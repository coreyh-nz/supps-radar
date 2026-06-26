package nz.coreyh.suppsradar.retailer.domain.model.crawl

import nz.coreyh.suppsradar.common.domain.model.audit.Auditable
import nz.coreyh.suppsradar.common.domain.model.audit.Deletable
import kotlin.time.Duration
import kotlin.time.Instant

/**
 * Represents a single crawl configuration for a retailer.
 */
data class CrawlSchedule(
    val id: CrawlScheduleId,
    val scope: CrawlScope,
    val interval: Duration,
    val enabled: Boolean,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val deletedAt: Instant?,
) : Auditable,
    Deletable
