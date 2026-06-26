package nz.coreyh.suppsradar.persistence.exposed

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.Instant

/**
 * Defines the auditing columns for tables that track creation and
 * modification timestamps.
 */
interface AuditColumns {
    val createdAt: Column<Instant>
    val updatedAt: Column<Instant>
}

/**
 * Adds a non-null `created_at` timestamp column to the table.
 */
fun Table.createdAt(): Column<Instant> = timestamp("created_at")

/**
 * Adds a non-null `updated_at` timestamp column to the table.
 */
fun Table.updatedAt(): Column<Instant> = timestamp("updated_at")
