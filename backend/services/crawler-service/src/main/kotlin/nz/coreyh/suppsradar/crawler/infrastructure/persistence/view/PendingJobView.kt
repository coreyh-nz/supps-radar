package nz.coreyh.suppsradar.crawler.infrastructure.persistence.view

import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobId

/**
 * Lightweight projection of a pending job, carrying only what the executor
 * needs to make dispatch decisions.
 */
data class PendingJobView(
    val jobId: CrawlJobId,
    val batchId: CrawlBatchId,
    val platformType: CrawlPlatformType,
    val batchStatus: CrawlStatus,
)
