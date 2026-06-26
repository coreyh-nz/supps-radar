package nz.coreyh.suppsradar.retailer.domain.repository

import nz.coreyh.suppsradar.retailer.domain.model.Retailer
import nz.coreyh.suppsradar.retailer.domain.model.RetailerId
import nz.coreyh.suppsradar.retailer.domain.model.RetailerPlatformType

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
    fun findAll(
        name: String?,
        slug: String?,
        platformTypes: Set<RetailerPlatformType>?,
        deleted: Boolean?,
    ): List<Retailer>

    /**
     * Retrieves a single retailer by its identifier.
     */
    fun findById(id: RetailerId): Retailer?
}
