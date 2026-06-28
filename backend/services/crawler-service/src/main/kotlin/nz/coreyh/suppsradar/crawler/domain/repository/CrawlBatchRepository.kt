package nz.coreyh.suppsradar.crawler.domain.repository

import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatch
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId

/**
 * Repository for persisting and querying [CrawlBatch] entities.
 */
interface CrawlBatchRepository {
    /**
     * Creates a new crawl batch or updates an existing one.
     */
    fun save(batch: CrawlBatch)

    /**
     * Retrieves all crawl batches matching the provided optional filters.
     *
     * Any filter set to `null` is ignored. Filters combine using logical AND.
     */
    fun findAll(
        retailers: Set<RetailerId>? = null,
        status: Set<CrawlStatus>? = null,
        deleted: Boolean? = null,
    ): List<CrawlBatch>

    /**
     * Retrieves a single batch by its identifier.
     */
    fun findById(id: CrawlBatchId): CrawlBatch?
}
