package nz.coreyh.suppsradar.crawler.domain.model.job

import nz.coreyh.suppsradar.common.domain.model.audit.Auditable
import nz.coreyh.suppsradar.common.domain.model.audit.Deletable
import nz.coreyh.suppsradar.common.domain.model.id.Identifiable
import nz.coreyh.suppsradar.crawler.domain.model.CrawlLifecycle
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatch
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId
import kotlin.time.Instant

/**
 * Represents a single unit of crawl work within a [CrawlBatch].
 *
 * Each job corresponds to crawling a single page, endpoint, or
 * resource. Jobs are executed by workers based on the retailer's
 * [CrawlPlatformType].
 */
data class CrawlJob(
    override val id: CrawlJobId,
    val batchId: CrawlBatchId,
    val retailerId: RetailerId,
    val metadata: CrawlJobMetadata,
    val attempts: Int,
    val maxAttempts: Int,
    val error: String?,
    override val status: CrawlStatus,
    override val startedAt: Instant?,
    override val completedAt: Instant?,
    override val failedAt: Instant?,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val deletedAt: Instant?,
) : Identifiable<CrawlJobId>,
    Auditable,
    Deletable,
    CrawlLifecycle
