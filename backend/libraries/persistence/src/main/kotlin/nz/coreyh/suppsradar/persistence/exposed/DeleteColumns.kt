package nz.coreyh.suppsradar.persistence.exposed

import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.Instant

/**
 * Defines the soft-delete column for tables that support logical deletion.
 */
interface DeleteColumns {
    val deletedAt: Column<Instant?>
}

/**
 * Adds a nullable `deleted_at` timestamp column to the table.
 */
fun Table.deletedAt() = timestamp("deleted_at").nullable()
