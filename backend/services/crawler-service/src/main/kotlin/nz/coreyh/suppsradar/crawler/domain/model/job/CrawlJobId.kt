package nz.coreyh.suppsradar.crawler.domain.model.job

import nz.coreyh.suppsradar.common.domain.model.id.DomainId
import nz.coreyh.suppsradar.common.domain.model.id.Id

/**
 * Strongly-typed identifier for a [CrawlJob].
 */
@JvmInline
value class CrawlJobId(
    val value: Id,
) : DomainId {
    override fun toString(): String = value.toString()
}
