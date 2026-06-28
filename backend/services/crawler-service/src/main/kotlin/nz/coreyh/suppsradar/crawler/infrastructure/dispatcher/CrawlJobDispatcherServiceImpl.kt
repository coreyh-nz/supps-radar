package nz.coreyh.suppsradar.crawler.infrastructure.dispatcher

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import nz.coreyh.suppsradar.crawler.application.service.CrawlJobExecutionService
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobResult
import nz.coreyh.suppsradar.crawler.domain.registry.CrawlBatchConcurrencyRegistry
import nz.coreyh.suppsradar.crawler.domain.registry.CrawlJobFactoryRegistry
import nz.coreyh.suppsradar.crawler.domain.registry.CrawlWorkerRegistry
import nz.coreyh.suppsradar.crawler.domain.service.CrawlJobDispatcherService
import nz.coreyh.suppsradar.crawler.infrastructure.persistence.repository.CrawlJobDispatcherRepository
import nz.coreyh.suppsradar.crawler.infrastructure.persistence.view.PendingJobView
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.springframework.stereotype.Service
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalAtomicApi::class)
@Service
class CrawlJobDispatcherServiceImpl(
    private val crawlJobExecutionService: CrawlJobExecutionService,
    private val crawlJobDispatcherRepository: CrawlJobDispatcherRepository,
    private val crawlWorkerRegistry: CrawlWorkerRegistry,
    private val crawlJobFactoryRegistry: CrawlJobFactoryRegistry,
    private val batchConcurrencyRegistry: CrawlBatchConcurrencyRegistry,
) : CrawlJobDispatcherService {
    // prevents re-entrant ticks. if the scheduler fires again before the
    // previous dispatch loop finishes, the new tick is dropped.
    private var tickRunning = AtomicBoolean(false)

    // dedicated scope for non-blocking job coroutines. SupervisorJob ensures
    // that one failing coroutine does not cancel sibling jobs.
    private val jobScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun tick() {
        if (!tickRunning.compareAndSet(expectedValue = false, newValue = true)) {
            logger.debug { "Scheduler tick skipped - previous dispatch is still in progress" }
            return
        }

        try {
            dispatch()
        } finally {
            tickRunning.store(false)
        }
    }

    private fun dispatch() =
        transaction {
            val candidates = crawlJobDispatcherRepository.findPendingJobsForDispatch()
            if (candidates.isEmpty()) {
                logger.debug { "No pending jobs found" }
                return@transaction
            }

            val counts =
                candidates
                    .map { tryDispatchJob(it) }
                    .groupingBy { it }
                    .eachCount()
            val dispatched = counts.getOrDefault(DispatchAttemptResult.DISPATCHED, 0)
            val skippedBatchLimit = counts.getOrDefault(DispatchAttemptResult.SKIPPED_BATCH_LIMIT, 0)
            val skippedClaimed = counts.getOrDefault(DispatchAttemptResult.SKIPPED_CLAIMED, 0)

            logger.info {
                buildString {
                    append("Dispatch complete - ")
                    append("dispatched=$dispatched, ")
                    append("skipped(batchLimit)=$skippedBatchLimit, ")
                    append("skipped(alreadyClaimed)=$skippedClaimed, ")
                }
            }
        }

    private fun tryDispatchJob(candidate: PendingJobView): DispatchAttemptResult {
        // try to acquire batch slot
        val acquiredBatchSlot = batchConcurrencyRegistry.tryAcquireOrIncrement(candidate.batchId)
        if (!acquiredBatchSlot) {
            logger.debug {
                "[job=${candidate.jobId}] [batch=${candidate.batchId}] Skipped - batch slot limit reached"
            }
            return DispatchAttemptResult.SKIPPED_BATCH_LIMIT
        }

        // load the full job
        val job =
            crawlJobDispatcherRepository.findByIdAndTryClaim(candidate.jobId) ?: run {
                logger.error {
                    "[job=${candidate.jobId}] Job not found after claim — releasing slots"
                }
                batchConcurrencyRegistry.decrementJobCount(candidate.batchId)
                return DispatchAttemptResult.SKIPPED_CLAIMED
            }

        // launch the job
        val worker =
            crawlWorkerRegistry.getWorker<
                CrawlJobMetadata,
                CrawlJobResult,
            >(candidate.platformType)
        val factory =
            crawlJobFactoryRegistry.getFactory<
                CrawlBatchMetadata,
                CrawlJobMetadata,
                CrawlJobResult,
            >(candidate.platformType)

        jobScope.launch {
            try {
                crawlJobExecutionService.execute(worker, factory, job, job.metadata)
            } finally {
                batchConcurrencyRegistry.decrementJobCount(candidate.batchId)
            }
        }

        logger.debug {
            "[job=${candidate.jobId}] [batch=${candidate.batchId}] [worker=${worker::class.simpleName}] Dispatched"
        }
        return DispatchAttemptResult.DISPATCHED
    }

    private enum class DispatchAttemptResult {
        DISPATCHED,
        SKIPPED_BATCH_LIMIT,
        SKIPPED_CLAIMED,
    }
}
