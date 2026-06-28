package nz.coreyh.suppsradar.crawler.infrastructure.persistence.table

import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import org.jetbrains.exposed.v1.core.Column
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp
import kotlin.time.Instant

interface CrawlLifecycleColumns {
    val status: Column<CrawlStatus>
    val startedAt: Column<Instant?>
    val completedAt: Column<Instant?>
    val failedAt: Column<Instant?>
}

fun Table.crawlStatus(): Column<CrawlStatus> = enumerationByName("crawl_status", 32)

fun Table.startedAt(): Column<Instant?> = timestamp("started_at").nullable()

fun Table.completedAt(): Column<Instant?> = timestamp("completed_at").nullable()

fun Table.failedAt(): Column<Instant?> = timestamp("failed_at").nullable()
