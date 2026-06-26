package nz.coreyh.suppsradar.retailer.domain.model.crawl

import nz.coreyh.suppsradar.common.domain.model.id.DomainId
import nz.coreyh.suppsradar.common.domain.model.id.Id

/**
 * Strongly-typed identifier for a [CrawlSchedule].
 */
@JvmInline
value class CrawlScheduleId(
    val value: Id,
) : DomainId {
    override fun toString(): String = value.toString()
}
