package nz.coreyh.suppsradar.retailer.infrastructure.persistence.table

import nz.coreyh.suppsradar.persistence.exposed.AuditColumns
import nz.coreyh.suppsradar.persistence.exposed.DeleteColumns
import nz.coreyh.suppsradar.persistence.exposed.createdAt
import nz.coreyh.suppsradar.persistence.exposed.deletedAt
import nz.coreyh.suppsradar.persistence.exposed.updatedAt
import nz.coreyh.suppsradar.retailer.domain.model.RetailerPlatformType
import org.jetbrains.exposed.v1.core.dao.id.UuidTable

object RetailerTable : UuidTable("retailer"), AuditColumns, DeleteColumns {
    val name = varchar("name", 255)
    val slug = varchar("slug", 255).uniqueIndex()
    val url = varchar("url", 255)
    val apiUrl = varchar("apiUrl", 255)
    val platformType = enumerationByName<RetailerPlatformType>("platform_type", 64)

    override val createdAt = createdAt()
    override val updatedAt = updatedAt()
    override val deletedAt = deletedAt()
}
