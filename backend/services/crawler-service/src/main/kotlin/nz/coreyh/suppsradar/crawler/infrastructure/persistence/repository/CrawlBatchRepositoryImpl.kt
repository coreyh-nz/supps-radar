package nz.coreyh.suppsradar.crawler.infrastructure.persistence.repository

import nz.coreyh.suppsradar.common.domain.model.id.Id
import nz.coreyh.suppsradar.crawler.domain.model.CrawlScope
import nz.coreyh.suppsradar.crawler.domain.model.CrawlScopeType
import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatch
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchMetadata
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId
import nz.coreyh.suppsradar.crawler.domain.repository.CrawlBatchRepository
import nz.coreyh.suppsradar.crawler.infrastructure.persistence.table.CrawlBatchTable
import nz.coreyh.suppsradar.persistence.exposed.expression.isDeleted
import nz.coreyh.suppsradar.persistence.exposed.expression.isNotDeleted
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.stereotype.Repository

@Repository
class CrawlBatchRepositoryImpl : CrawlBatchRepository {
    override fun save(batch: CrawlBatch): Unit =
        transaction {
            CrawlBatchTable.upsert(CrawlBatchTable.id) {
                it[CrawlBatchTable.id] = batch.id.value.value
                it[CrawlBatchTable.platformType] = batch.platformType
                it[CrawlBatchTable.scopeType] = batch.scope.type
                it[CrawlBatchTable.scopeValue] =
                    when (batch.scope) {
                        is CrawlScope.Full -> null
                        is CrawlScope.Category -> batch.scope.value
                    }
                it[CrawlBatchTable.status] = batch.status
                it[CrawlBatchTable.startedAt] = batch.startedAt
                it[CrawlBatchTable.completedAt] = batch.completedAt
                it[CrawlBatchTable.failedAt] = batch.failedAt
                it[CrawlBatchTable.createdAt] = batch.createdAt
                it[CrawlBatchTable.updatedAt] = batch.updatedAt
                it[CrawlBatchTable.deletedAt] = batch.deletedAt
            }
        }

    override fun findAll(
        retailers: Set<RetailerId>?,
        status: Set<CrawlStatus>?,
        deleted: Boolean?,
    ): List<CrawlBatch> =
        transaction {
            CrawlBatchTable
                .selectAll()
                .apply {
                    retailers?.let {
                        andWhere { CrawlBatchTable.retailerId inList retailers.map { it.value.value } }
                    }
                    status?.let {
                        andWhere { CrawlBatchTable.status inList status }
                    }
                    deleted?.let {
                        andWhere { if (it) CrawlBatchTable.isDeleted() else CrawlBatchTable.isNotDeleted() }
                    }
                }.map { it.toCrawlBatch() }
        }

    override fun findById(id: CrawlBatchId): CrawlBatch? =
        transaction {
            CrawlBatchTable
                .selectAll()
                .where { CrawlBatchTable.id eq id.value.value }
                .singleOrNull()
                ?.toCrawlBatch()
        }

    private fun ResultRow.toCrawlBatch(): CrawlBatch {
        val scope =
            when (this[CrawlBatchTable.scopeType]) {
                CrawlScopeType.FULL -> CrawlScope.Full
                CrawlScopeType.CATEGORY -> CrawlScope.Category(this[CrawlBatchTable.scopeValue] ?: "")
            }
        return CrawlBatch(
            id = CrawlBatchId(Id(this[CrawlBatchTable.id].value)),
            retailerId = RetailerId(Id(this[CrawlBatchTable.id].value)),
            platformType = this[CrawlBatchTable.platformType],
            scope = scope,
            metadata = object : CrawlBatchMetadata {}, // TODO
            status = this[CrawlBatchTable.status],
            startedAt = this[CrawlBatchTable.startedAt],
            completedAt = this[CrawlBatchTable.completedAt],
            failedAt = this[CrawlBatchTable.failedAt],
            createdAt = this[CrawlBatchTable.createdAt],
            updatedAt = this[CrawlBatchTable.updatedAt],
            deletedAt = this[CrawlBatchTable.deletedAt],
        )
    }
}
