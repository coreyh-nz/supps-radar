package nz.coreyh.suppsradar.crawler.infrastructure.registry

import nz.coreyh.suppsradar.crawler.application.config.CrawlExecutorProperties
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId
import nz.coreyh.suppsradar.crawler.domain.registry.CrawlBatchConcurrencyRegistry
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Component
class CrawlBatchConcurrencyRegistryInMemoryRegistry(
    private val crawlExecutorProperties: CrawlExecutorProperties,
) : CrawlBatchConcurrencyRegistry {
    private val activeBatches: ConcurrentHashMap<CrawlBatchId, AtomicInteger> = ConcurrentHashMap()
    private val batchSlotCount = AtomicInteger(0)

    override fun tryAcquireOrIncrement(id: CrawlBatchId): Boolean {
        // if batch is already active, just increment its job count.
        activeBatches[id]?.let { counter ->
            counter.incrementAndGet()
            return true
        }

        // otherwise attempt to acquire a new batch slot.
        // uses lock-free CAS
        while (true) {
            val current = batchSlotCount.get()
            if (current >= crawlExecutorProperties.maxConcurrentBatches) return false
            if (batchSlotCount.compareAndSet(current, current + 1)) {
                activeBatches[id] = AtomicInteger(1)
                return true
            }
        }
    }

    override fun decrementJobCount(id: CrawlBatchId) {
        activeBatches[id]?.decrementAndGet()
            ?: error(
                "Attempted to decrement job count for batch '$id' " +
                    "which is not registered in BatchConcurrencyRegistry.",
            )
    }

    override fun activeBatchCount(): Int = batchSlotCount.get()
}
