package nz.coreyh.suppsradar.crawler.application.service

import nz.coreyh.suppsradar.crawler.domain.exception.CrawlBatchNotFoundException
import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatch
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId
import nz.coreyh.suppsradar.crawler.domain.repository.CrawlBatchRepository
import org.springframework.stereotype.Service
import kotlin.time.Clock

@Service
class CrawlBatchService(
    private val crawlBatchRepository: CrawlBatchRepository,
    private val clock: Clock = Clock.System,
) {
    fun markStarted(id: CrawlBatchId) {
        val batch = findOrThrow(id)
        val now = clock.now()
        crawlBatchRepository.save(
            batch.copy(
                status = CrawlStatus.RUNNING,
                completedAt = now,
                updatedAt = now,
            ),
        )
    }

    fun markFailed(id: CrawlBatchId) {
        val batch = findOrThrow(id)
        val now = clock.now()
        crawlBatchRepository.save(
            batch.copy(
                status = CrawlStatus.FAILED,
                failedAt = now,
                updatedAt = now,
            ),
        )
    }

    fun markCompleted(id: CrawlBatchId) {
        val batch = findOrThrow(id)
        val now = clock.now()
        crawlBatchRepository.save(
            batch.copy(
                status = CrawlStatus.COMPLETED,
                completedAt = now,
                updatedAt = now,
            ),
        )
    }

    private fun findOrThrow(id: CrawlBatchId): CrawlBatch = crawlBatchRepository.findById(id) ?: throw CrawlBatchNotFoundException()
}
