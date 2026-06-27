package nz.coreyh.suppsradar.crawler.infrastructure.persistence.table

import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.persistence.exposed.AuditColumns
import nz.coreyh.suppsradar.persistence.exposed.createdAt
import nz.coreyh.suppsradar.persistence.exposed.updatedAt
import org.jetbrains.exposed.v1.core.dao.id.UuidTable

object RetailerTable : UuidTable("crawl_retailer"), AuditColumns {
    val apiUrl = varchar("api_url", 256)
    val platformType = enumerationByName<CrawlPlatformType>("platform_type", 64)

    override val createdAt = createdAt()
    override val updatedAt = updatedAt()
}
