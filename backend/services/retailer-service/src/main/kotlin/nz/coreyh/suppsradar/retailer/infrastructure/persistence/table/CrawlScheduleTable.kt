package nz.coreyh.suppsradar.retailer.infrastructure.persistence.table

import nz.coreyh.suppsradar.persistence.exposed.AuditColumns
import nz.coreyh.suppsradar.persistence.exposed.DeleteColumns
import nz.coreyh.suppsradar.persistence.exposed.createdAt
import nz.coreyh.suppsradar.persistence.exposed.deletedAt
import nz.coreyh.suppsradar.persistence.exposed.updatedAt
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlScopeType
import org.jetbrains.exposed.v1.core.dao.id.UuidTable
import org.jetbrains.exposed.v1.datetime.duration

object CrawlScheduleTable :
    UuidTable("retailer_crawl_schedule"),
    AuditColumns,
    DeleteColumns {
    val retailerId = reference("retailer_id", RetailerTable)
    val scopeType = enumerationByName<CrawlScopeType>("scope_type", 64)
    val scopeValue = varchar("scope_value", 255).nullable() // null for FULL
    val interval = duration("interval")
    val enabled = bool("enabled")
    override val createdAt = createdAt()
    override val updatedAt = updatedAt()
    override val deletedAt = deletedAt()
}
