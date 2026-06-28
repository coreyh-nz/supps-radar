package nz.coreyh.suppsradar.crawler.infrastructure.persistence.table

import nz.coreyh.suppsradar.persistence.exposed.AuditColumns
import nz.coreyh.suppsradar.persistence.exposed.DeleteColumns
import nz.coreyh.suppsradar.persistence.exposed.createdAt
import nz.coreyh.suppsradar.persistence.exposed.deletedAt
import nz.coreyh.suppsradar.persistence.exposed.updatedAt
import org.jetbrains.exposed.v1.core.dao.id.UuidTable

object CrawlJobTable : UuidTable("crawl_job"), CrawlLifecycleColumns, AuditColumns, DeleteColumns {
    val batchId = reference("batch_id", CrawlBatchTable)
    val attempts = integer("attempts")
    val maxAttempts = integer("max_attempts")
    val error = text("error").nullable()

    override val status = crawlStatus()
    override val startedAt = startedAt()
    override val completedAt = completedAt()
    override val failedAt = failedAt()
    override val createdAt = createdAt()
    override val updatedAt = updatedAt()
    override val deletedAt = deletedAt()
}
