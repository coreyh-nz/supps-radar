package nz.coreyh.suppsradar.retailer.infrastructure.persistence.repository

import nz.coreyh.suppsradar.common.domain.model.id.Id
import nz.coreyh.suppsradar.persistence.exposed.expression.isDeleted
import nz.coreyh.suppsradar.persistence.exposed.expression.isNotDeleted
import nz.coreyh.suppsradar.retailer.domain.model.RetailerId
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlSchedule
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlScheduleId
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlScope
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlScopeType
import nz.coreyh.suppsradar.retailer.domain.repository.CrawlScheduleRepository
import nz.coreyh.suppsradar.retailer.infrastructure.persistence.table.CrawlScheduleTable
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.inList
import org.jetbrains.exposed.v1.jdbc.andWhere
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.upsert
import org.springframework.stereotype.Repository

@Repository
class CrawlScheduleRepositoryImpl : CrawlScheduleRepository {
    override fun save(schedule: CrawlSchedule): Unit =
        transaction {
            CrawlScheduleTable.upsert(CrawlScheduleTable.id) {
                it[CrawlScheduleTable.id] = schedule.id.value.value
                it[CrawlScheduleTable.scopeType] = schedule.scope.type
                it[CrawlScheduleTable.scopeValue] =
                    when (schedule.scope) {
                        is CrawlScope.Full -> null
                        is CrawlScope.Category -> schedule.scope.value
                    }
                it[CrawlScheduleTable.interval] = schedule.interval
                it[CrawlScheduleTable.enabled] = schedule.enabled
                it[CrawlScheduleTable.createdAt] = schedule.createdAt
                it[CrawlScheduleTable.updatedAt] = schedule.updatedAt
                it[CrawlScheduleTable.deletedAt] = schedule.deletedAt
            }
        }

    override fun findAll(
        retailerIds: Set<RetailerId>?,
        scopeTypes: List<CrawlScopeType>?,
        enabled: Boolean?,
        deleted: Boolean?,
    ): List<CrawlSchedule> =
        transaction {
            CrawlScheduleTable
                .selectAll()
                .apply {
                    retailerIds?.let {
                        andWhere { CrawlScheduleTable.retailerId inList retailerIds.map { it.value.value } }
                    }
                    scopeTypes?.let {
                        andWhere { CrawlScheduleTable.scopeType inList scopeTypes }
                    }
                    enabled?.let {
                        andWhere { CrawlScheduleTable.enabled eq enabled }
                    }
                    deleted?.let {
                        andWhere {
                            if (deleted) CrawlScheduleTable.isDeleted() else CrawlScheduleTable.isNotDeleted()
                        }
                    }
                }.map { it.toCrawlSchedule() }
        }

    override fun findById(scheduleId: CrawlScheduleId): CrawlSchedule? =
        transaction {
            CrawlScheduleTable
                .selectAll()
                .where { CrawlScheduleTable.id eq scheduleId.value.value }
                .singleOrNull()
                ?.toCrawlSchedule()
        }

    fun ResultRow.toCrawlSchedule(): CrawlSchedule {
        val scope =
            when (this[CrawlScheduleTable.scopeType]) {
                CrawlScopeType.FULL -> CrawlScope.Full
                CrawlScopeType.CATEGORY -> CrawlScope.Category(this[CrawlScheduleTable.scopeValue] ?: "")
            }
        return CrawlSchedule(
            id = CrawlScheduleId(Id(this[CrawlScheduleTable.id].value)),
            scope = scope,
            interval = this[CrawlScheduleTable.interval],
            enabled = this[CrawlScheduleTable.enabled],
            createdAt = this[CrawlScheduleTable.createdAt],
            updatedAt = this[CrawlScheduleTable.updatedAt],
            deletedAt = this[CrawlScheduleTable.deletedAt],
        )
    }
}
