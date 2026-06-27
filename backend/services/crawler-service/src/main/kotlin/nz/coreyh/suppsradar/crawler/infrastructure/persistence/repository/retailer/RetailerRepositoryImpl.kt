package nz.coreyh.suppsradar.crawler.infrastructure.persistence.repository.retailer

import nz.coreyh.suppsradar.common.domain.model.id.Id
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.retailer.Retailer
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId
import nz.coreyh.suppsradar.crawler.domain.repository.retailer.RetailerRepository
import nz.coreyh.suppsradar.crawler.infrastructure.persistence.table.RetailerTable
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.stereotype.Repository

@Repository
class RetailerRepositoryImpl : RetailerRepository {
    override fun save(retailer: Retailer): Unit =
        transaction {
            RetailerTable.upsert(RetailerTable.id) {
                it[RetailerTable.id] = retailer.id.value.value
                it[RetailerTable.apiUrl] = retailer.apiUrl
                it[RetailerTable.platformType] = retailer.platformType
                it[RetailerTable.createdAt] = retailer.createdAt
                it[RetailerTable.updatedAt] = retailer.updatedAt
            }
        }

    override fun findAll(platformTypes: Set<CrawlPlatformType>?): List<Retailer> =
        transaction {
            RetailerTable
                .selectAll()
                .apply {
                    platformTypes?.let {
                        andWhere { RetailerTable.platformType inList it }
                    }
                }.map { it.toRetailer() }
        }

    override fun findById(id: RetailerId): Retailer? =
        transaction {
            RetailerTable
                .selectAll()
                .where { RetailerTable.id eq id.value.value }
                .singleOrNull()
                ?.toRetailer()
        }

    private fun ResultRow.toRetailer(): Retailer =
        Retailer(
            id = RetailerId(Id(this[RetailerTable.id].value)),
            platformType = this[RetailerTable.platformType],
            apiUrl = this[RetailerTable.apiUrl],
            createdAt = this[RetailerTable.createdAt],
            updatedAt = this[RetailerTable.updatedAt],
        )
}
