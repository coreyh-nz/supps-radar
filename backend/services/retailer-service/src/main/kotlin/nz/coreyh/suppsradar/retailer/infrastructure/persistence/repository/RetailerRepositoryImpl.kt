package nz.coreyh.suppsradar.retailer.infrastructure.persistence.repository

import nz.coreyh.suppsradar.common.domain.model.id.Id
import nz.coreyh.suppsradar.persistence.exposed.expression.isDeleted
import nz.coreyh.suppsradar.persistence.exposed.expression.isNotDeleted
import nz.coreyh.suppsradar.retailer.domain.model.Retailer
import nz.coreyh.suppsradar.retailer.domain.model.RetailerId
import nz.coreyh.suppsradar.retailer.domain.model.RetailerPlatformType
import nz.coreyh.suppsradar.retailer.domain.repository.RetailerRepository
import nz.coreyh.suppsradar.retailer.infrastructure.persistence.table.RetailerTable
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.like
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.stereotype.Repository

@Repository
class RetailerRepositoryImpl : RetailerRepository {
    override fun save(retailer: Retailer): Unit =
        transaction {
            RetailerTable.upsert(
                RetailerTable.id,
                onUpdateExclude = listOf(RetailerTable.createdAt),
            ) {
                it[RetailerTable.id] = retailer.id.value.value
                it[RetailerTable.name] = retailer.name
                it[RetailerTable.slug] = retailer.slug
                it[RetailerTable.url] = retailer.url
                it[RetailerTable.apiUrl] = retailer.apiUrl
                it[RetailerTable.platformType] = retailer.platformType
                it[RetailerTable.createdAt] = retailer.createdAt
                it[RetailerTable.updatedAt] = retailer.updatedAt
                it[RetailerTable.deletedAt] = retailer.deletedAt
            }
        }

    override fun findAll(
        name: String?,
        slug: String?,
        platformTypes: Set<RetailerPlatformType>?,
        deleted: Boolean?,
    ): List<Retailer> =
        transaction {
            RetailerTable
                .selectAll()
                .apply {
                    name?.let { andWhere { RetailerTable.name like name } }
                    slug?.let { andWhere { RetailerTable.slug like slug } }
                    platformTypes?.let {
                        andWhere { RetailerTable.platformType inList platformTypes }
                    }
                    deleted?.let {
                        andWhere {
                            if (deleted) RetailerTable.isDeleted() else RetailerTable.isNotDeleted()
                        }
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
            name = this[RetailerTable.name],
            slug = this[RetailerTable.slug],
            url = this[RetailerTable.url],
            apiUrl = this[RetailerTable.apiUrl],
            platformType = this[RetailerTable.platformType],
            createdAt = this[RetailerTable.createdAt],
            updatedAt = this[RetailerTable.updatedAt],
            deletedAt = this[RetailerTable.deletedAt],
        )
}
