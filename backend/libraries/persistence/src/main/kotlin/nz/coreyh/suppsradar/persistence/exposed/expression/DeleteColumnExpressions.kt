package nz.coreyh.suppsradar.persistence.exposed.expression

import nz.coreyh.suppsradar.persistence.exposed.DeleteColumns
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.greater
import org.jetbrains.exposed.v1.core.isNotNull
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.core.less
import kotlin.time.Instant

/**
 * Returns an expression matching rows that have been soft-deleted.
 */
fun DeleteColumns.isDeleted(): Op<Boolean> = deletedAt.isNotNull()

/**
 * Returns an expression matching rows that have NOT been soft-deleted.
 */
fun DeleteColumns.isNotDeleted(): Op<Boolean> = deletedAt.isNull()

/**
 * Matches rows deleted before the given timestamp.
 */
fun DeleteColumns.deletedBefore(instant: Instant): Op<Boolean> = deletedAt.less(instant)

/**
 * Matches rows deleted after the given timestamp.
 */
fun DeleteColumns.deletedAfter(instant: Instant): Op<Boolean> = deletedAt.greater(instant)
