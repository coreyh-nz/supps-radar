package nz.coreyh.suppsradar.crawler.domain.repository.retailer

import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.retailer.Retailer
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId

/**
 * Repository for persisting and querying [Retailer] entities.
 */
interface RetailerRepository {
    /**
     * Creates a new retailer or updates an existing one.
     */
    fun save(retailer: Retailer)

    /**
     * Retrieves all retailers matching the provided optional filters.
     *
     * Any filter set to `null` is ignored. Filters combine using logical AND.
     */
    fun findAll(platformTypes: Set<CrawlPlatformType>? = null): List<Retailer>

    /**
     * Retrieves a single retailer config by its retailer identifier.
     */
    fun findById(id: RetailerId): Retailer?
}
