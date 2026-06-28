package nz.coreyh.suppsradar.crawler.application.service

import nz.coreyh.suppsradar.crawler.domain.exception.CrawlJobNotFoundException
import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJob
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobId
import nz.coreyh.suppsradar.crawler.domain.repository.CrawlJobRepository
import org.springframework.stereotype.Service
import kotlin.time.Clock

@Service
class CrawlJobService(
    private val crawlJobRepository: CrawlJobRepository,
    private val clock: Clock = Clock.System,
) {
    fun markStarted(id: CrawlJobId) {
        val job = findOrThrow(id)
        val now = clock.now()
        crawlJobRepository.save(
            job.copy(
                status = CrawlStatus.RUNNING,
                startedAt = now,
                updatedAt = now,
            ),
        )
    }

    fun markFailed(id: CrawlJobId) {
        val job = findOrThrow(id)
        val now = clock.now()
        crawlJobRepository.save(
            job.copy(
                status = CrawlStatus.FAILED,
                failedAt = now,
                updatedAt = now,
            ),
        )
    }

    fun markCompleted(id: CrawlJobId) {
        val job = findOrThrow(id)
        val now = clock.now()
        crawlJobRepository.save(
            job.copy(
                status = CrawlStatus.COMPLETED,
                completedAt = now,
                updatedAt = now,
            ),
        )
    }

    fun incrementAttempts(id: CrawlJobId) {
        val job = findOrThrow(id)
        val now = clock.now()
        crawlJobRepository.save(
            job.copy(
                attempts = job.attempts + 1,
                updatedAt = now,
            ),
        )
    }

    private fun findOrThrow(id: CrawlJobId): CrawlJob = crawlJobRepository.findById(id) ?: throw CrawlJobNotFoundException()
}
