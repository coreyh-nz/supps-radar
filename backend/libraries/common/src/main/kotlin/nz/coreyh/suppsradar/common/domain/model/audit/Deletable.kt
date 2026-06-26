package nz.coreyh.suppsradar.common.domain.model.audit

import kotlin.time.Instant

/**
 * Indicates that a domain model supports soft deletion.
 *
 * An entity is considered deleted when [deletedAt] is non-null.
 */
interface Deletable {
    val deletedAt: Instant?
}

/**
 * Convenience property that returns true when the entity has been soft-deleted.
 */
val Deletable.deleted
    get() = deletedAt != null
