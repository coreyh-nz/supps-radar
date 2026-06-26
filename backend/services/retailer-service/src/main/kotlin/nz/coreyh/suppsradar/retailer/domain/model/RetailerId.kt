package nz.coreyh.suppsradar.retailer.domain.model

import nz.coreyh.suppsradar.common.domain.model.id.DomainId
import nz.coreyh.suppsradar.common.domain.model.id.Id

/**
 * Strongly-typed identifier for a [Retailer].
 */
@JvmInline
value class RetailerId(
    val value: Id,
) : DomainId {
    override fun toString(): String = value.toString()
}
