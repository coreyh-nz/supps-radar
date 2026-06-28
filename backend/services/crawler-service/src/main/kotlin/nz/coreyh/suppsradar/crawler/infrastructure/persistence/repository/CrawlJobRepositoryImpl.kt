package nz.coreyh.suppsradar.crawler.infrastructure.persistence.repository

import nz.coreyh.suppsradar.common.domain.model.id.Id
import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJob
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobId
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobMetadata
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId
import nz.coreyh.suppsradar.crawler.domain.repository.CrawlJobRepository
import nz.coreyh.suppsradar.crawler.infrastructure.persistence.table.CrawlBatchTable
import nz.coreyh.suppsradar.crawler.infrastructure.persistence.table.CrawlJobTable
import nz.coreyh.suppsradar.persistence.exposed.expression.isDeleted
import nz.coreyh.suppsradar.persistence.exposed.expression.isNotDeleted
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.core.isNotNull
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.stereotype.Repository

@Repository
class CrawlJobRepositoryImpl : CrawlJobRepository {
    override fun save(job: CrawlJob): Unit =
        transaction {
            CrawlJobTable.upsert(CrawlJobTable.id) {
                it[CrawlJobTable.id] = job.id.value.value
                it[CrawlJobTable.batchId] = job.batchId.value.value
                it[CrawlJobTable.attempts] = job.attempts
                it[CrawlJobTable.attempts] = job.attempts
                it[CrawlJobTable.maxAttempts] = job.maxAttempts
                it[CrawlJobTable.error] = job.error
                it[CrawlJobTable.status] = job.status
                it[CrawlJobTable.startedAt] = job.startedAt
                it[CrawlJobTable.completedAt] = job.completedAt
                it[CrawlJobTable.failedAt] = job.failedAt
                it[CrawlJobTable.createdAt] = job.createdAt
                it[CrawlJobTable.updatedAt] = job.updatedAt
                it[CrawlJobTable.deletedAt] = job.deletedAt
            }
        }

    override fun findAll(
        batches: Set<CrawlBatchId>?,
        retailers: Set<RetailerId>?,
        status: Set<CrawlStatus>?,
        error: Boolean?,
        deleted: Boolean?,
    ): List<CrawlJob> =
        transaction {
            (CrawlJobTable innerJoin CrawlBatchTable)
                .selectAll()
                .apply {
                    retailers?.let {
                        andWhere { CrawlBatchTable.retailerId inList retailers.map { it.value.value } }
                    }
                    status?.let {
                        andWhere { CrawlJobTable.status inList status }
                    }
                    error?.let {
                        andWhere { if (it) CrawlJobTable.error.isNotNull() else CrawlJobTable.error.isNull() }
                    }
                    deleted?.let {
                        andWhere { if (it) CrawlJobTable.isDeleted() else CrawlJobTable.isNotDeleted() }
                    }
                }.map { it.toCrawlJob() }
        }

    override fun findById(id: CrawlJobId): CrawlJob? =
        transaction {
            (CrawlJobTable innerJoin CrawlBatchTable)
                .selectAll()
                .where { CrawlBatchTable.id eq id.value.value }
                .singleOrNull()
                ?.toCrawlJob()
        }

    private fun ResultRow.toCrawlJob(): CrawlJob =
        CrawlJob(
            id = CrawlJobId(Id(this[CrawlJobTable.id].value)),
            batchId = CrawlBatchId(Id(this[CrawlJobTable.batchId].value)),
            retailerId = RetailerId(Id(this[CrawlBatchTable.retailerId])),
            status = this[CrawlBatchTable.status],
            metadata = object : CrawlJobMetadata {}, // TODO
            attempts = this[CrawlJobTable.attempts],
            maxAttempts = this[CrawlJobTable.maxAttempts],
            error = this[CrawlJobTable.error],
            startedAt = this[CrawlJobTable.startedAt],
            completedAt = this[CrawlJobTable.completedAt],
            failedAt = this[CrawlJobTable.failedAt],
            createdAt = this[CrawlJobTable.createdAt],
            updatedAt = this[CrawlJobTable.updatedAt],
            deletedAt = this[CrawlJobTable.deletedAt],
        )
}
