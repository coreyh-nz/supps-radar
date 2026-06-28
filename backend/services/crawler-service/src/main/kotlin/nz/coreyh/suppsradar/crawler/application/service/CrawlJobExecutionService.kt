package nz.coreyh.suppsradar.crawler.application.service

import io.github.oshai.kotlinlogging.KotlinLogging
import nz.coreyh.suppsradar.crawler.domain.factory.CrawlJobFactory
import nz.coreyh.suppsradar.crawler.domain.factory.createNextJob
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJob
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobResult
import nz.coreyh.suppsradar.crawler.domain.worker.CrawlWorker
import org.springframework.stereotype.Service
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock

private val logger = KotlinLogging.logger {}

@Service
class CrawlJobExecutionService(
    private val crawlBatchService: CrawlBatchService,
    private val crawlJobService: CrawlJobService,
    private val clock: Clock = Clock.System,
) {
    suspend fun <
        BM : CrawlBatchMetadata,
        JM : CrawlJobMetadata,
        R : CrawlJobResult,
    > execute(
        worker: CrawlWorker<JM, R>,
        factory: CrawlJobFactory<BM, JM, R>,
        job: CrawlJob,
        jobMetadata: JM,
    ) {
        crawlJobService.markStarted(job.id)
        logger.debug { "Job ${job.id} attempt ${job.attempts}/${job.maxAttempts} starting" }

        try {
            val result = worker.processJob(job, jobMetadata)
            crawlJobService.markCompleted(job.id)
            logger.debug { "Job ${job.id} attempt ${job.attempts}/${job.maxAttempts} completed" }

            factory
                .createNextJob(job, result, clock.now())
                ?.let {
                    logger.debug { "Enqueued next job for batch ${job.batchId}" }
                }
                ?: run {
                    crawlBatchService.markCompleted(job.batchId)
                    logger.debug { "Batch ${job.batchId} completed" }
                }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            if (job.attempts == job.maxAttempts) {
                crawlJobService.markFailed(job.id)
                logger.debug {
                    "Job ${job.id} attempt ${job.attempts}/${job.maxAttempts} failed and will not be tried again: ${e.message}"
                }

                crawlBatchService.markFailed(job.batchId)
                logger.debug { "Batch ${job.batchId} failed" }
                return
            }

            // TODO - could do some kind of exponential backoff
            //  currently will just run the next time the scheduler runs
            logger.debug {
                "Job ${job.id} attempt ${job.attempts}/${job.maxAttempts} failed: ${e.message}"
            }
            crawlJobService.incrementAttempts(job.id)
        }
    }
}
