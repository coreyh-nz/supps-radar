package nz.coreyh.suppsradar.retailer.domain.repository

import nz.coreyh.suppsradar.retailer.domain.model.RetailerId
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlSchedule
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlScheduleId
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlScopeType

/**
 * Repository for persisting and querying [CrawlSchedule] entities.
 */
interface CrawlScheduleRepository {
    /**
     * Creates a new crawl schedule or updates an existing one.
     */
    fun save(schedule: CrawlSchedule)

    /**
     * Retrieves all crawl schedules matching the provided optional filters.
     *
     * Any filter set to `null` is ignored. Filters combine using logical AND.
     */
    fun findAll(
        retailerIds: Set<RetailerId>? = null,
        scopeTypes: List<CrawlScopeType>? = null,
        enabled: Boolean? = null,
        deleted: Boolean? = null,
    ): List<CrawlSchedule>

    /**
     * Retrieves a single crawl schedule by its identifier.
     */
    fun findById(scheduleId: CrawlScheduleId): CrawlSchedule?
}
