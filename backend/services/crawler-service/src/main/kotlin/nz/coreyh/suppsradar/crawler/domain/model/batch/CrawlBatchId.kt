package nz.coreyh.suppsradar.crawler.domain.model.batch

import nz.coreyh.suppsradar.common.domain.model.id.DomainId
import nz.coreyh.suppsradar.common.domain.model.id.Id

/**
 * Strongly-typed identifier for a [CrawlBatch].
 */
@JvmInline
value class CrawlBatchId(
    val value: Id,
) : DomainId {
    override fun toString(): String = value.toString()
}
