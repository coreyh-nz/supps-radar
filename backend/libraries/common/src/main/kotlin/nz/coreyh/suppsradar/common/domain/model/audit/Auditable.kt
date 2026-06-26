package nz.coreyh.suppsradar.common.domain.model.audit

import kotlin.time.Instant

/**
 * Indicates that a domain model tracks creation and update timestamps.
 */
interface Auditable {
    val createdAt: Instant
    val updatedAt: Instant
}
