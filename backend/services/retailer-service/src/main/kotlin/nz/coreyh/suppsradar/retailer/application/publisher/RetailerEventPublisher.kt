package nz.coreyh.suppsradar.retailer.application.publisher

import nz.coreyh.suppsradar.retailer.domain.model.Retailer

/**
 * Publishes lifecycle events for a [Retailer] to external services.
 */
interface RetailerEventPublisher {
    /**
     * Publishes an event indicating that a new [Retailer] has been registered.
     */
    fun publishRegistered(retailer: Retailer)

    /**
     * Publishes an event indicating that an existing [Retailer] has been
     * updated.
     */
    fun publishUpdated(retailer: Retailer)

    /**
     * Publishes an event indicating that a [Retailer] has been deleted.
     */
    fun publishDeleted(retailer: Retailer)
}
