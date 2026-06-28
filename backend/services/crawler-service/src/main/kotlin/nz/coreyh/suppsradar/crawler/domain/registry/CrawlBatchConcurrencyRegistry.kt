package nz.coreyh.suppsradar.crawler.domain.registry

import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId

/**
 * Tracks which batches are currently occupying an executor slot on this
 * instance, and enforces the global maximum number of concurrently active
 * batches.
 *
 * "Active" means at least one job from the batch is currently being
 * executed by this instance. A batch that already has an active slot does
 * not consume an additional slot when more of its jobs are dispatched
 */
interface CrawlBatchConcurrencyRegistry {
    /**
     * Attempts to acquire a batch slot for [id], or increments the job count
     * if the batch is already active.
     *
     * @return `true` if the batch was already active (job count incremented),
     *    or a new slot was successfully acquired for this batch, `false` if
     *    the batch is not active AND the registry is at its maximum
     *    active-batch limit.
     */
    fun tryAcquireOrIncrement(id: CrawlBatchId): Boolean

    /**
     * Decrements the running-job count for [id]. When the count reaches zero
     * the batch slot is automatically released.
     */
    fun decrementJobCount(id: CrawlBatchId)

    /**
     * Returns the number of batch slots currently occupied on this instance.
     */
    fun activeBatchCount(): Int
}
