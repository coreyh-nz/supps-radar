package nz.coreyh.suppsradar.crawler.domain.model.retailer

import nz.coreyh.suppsradar.common.domain.model.audit.Auditable
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import kotlin.time.Instant

/**
 * Represents a merchant or storefront.
 */
data class Retailer(
    val id: RetailerId,
    val platformType: CrawlPlatformType,
    val apiUrl: String,
    override val createdAt: Instant,
    override val updatedAt: Instant,
) : Auditable
