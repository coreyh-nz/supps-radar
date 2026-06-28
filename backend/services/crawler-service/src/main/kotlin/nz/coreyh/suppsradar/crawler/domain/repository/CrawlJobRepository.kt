package nz.coreyh.suppsradar.crawler.domain.repository

import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJob
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobId
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId

/**
 * Repository for persisting and querying [CrawlJob] entities.
 */
interface CrawlJobRepository {
    /**
     * Creates a new crawl job or updates an existing one.
     */
    fun save(job: CrawlJob)

    /**
     * Retrieves all crawl jobs matching the provided optional filters.
     *
     * Any filter set to `null` is ignored. Filters combine using logical AND.
     *
     * @param batches restrict results to jobs belonging to these batch IDs
     * @param retailers restrict results to jobs associated with these
     *    retailers
     * @param status restrict results to jobs in one of these statuses
     * @param error if `true`, only return jobs that failed; if `false`, only
     *    return jobs that did not fail; if `null`, ignore this filter
     * @param deleted if `true`, return only soft‑deleted jobs; if `false`,
     *    return only non‑deleted jobs; if `null`, ignore this filter
     */
    fun findAll(
        batches: Set<CrawlBatchId>? = null,
        retailers: Set<RetailerId>? = null,
        status: Set<CrawlStatus>? = null,
        error: Boolean? = null,
        deleted: Boolean? = null,
    ): List<CrawlJob>

    /**
     * Retrieves a single job by its identifier.
     */
    fun findById(id: CrawlJobId): CrawlJob?
}
