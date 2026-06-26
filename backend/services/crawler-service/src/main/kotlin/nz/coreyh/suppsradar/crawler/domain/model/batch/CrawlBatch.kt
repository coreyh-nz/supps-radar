package nz.coreyh.suppsradar.crawler.domain.model.batch

import nz.coreyh.suppsradar.common.domain.model.audit.Auditable
import nz.coreyh.suppsradar.common.domain.model.audit.Deletable
import nz.coreyh.suppsradar.common.domain.model.id.Identifiable
import nz.coreyh.suppsradar.crawler.domain.model.CrawlLifecycle
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.CrawlScope
import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJob
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId
import kotlin.time.Instant

/**
 * Represents a unit of crawl work.
 *
 * A batch groups together multiple [CrawlJob]s that belong to the same
 * retailer and share the same crawl intent.
 */
data class CrawlBatch(
    override val id: CrawlBatchId,
    val retailerId: RetailerId,
    val platformType: CrawlPlatformType,
    val scope: CrawlScope,
    override val status: CrawlStatus,
    val metadata: CrawlBatchMetadata,
    override val startedAt: Instant?,
    override val completedAt: Instant?,
    override val failedAt: Instant?,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val deletedAt: Instant?,
) : Identifiable<CrawlBatchId>,
    Auditable,
    Deletable,
    CrawlLifecycle
