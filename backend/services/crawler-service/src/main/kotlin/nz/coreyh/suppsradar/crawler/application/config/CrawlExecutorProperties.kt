package nz.coreyh.suppsradar.crawler.application.config

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration

@ConfigurationProperties(prefix = "crawl.executor")
data class CrawlExecutorProperties(
    /**
     * Maximum number of batches this instance may have in-flight at once.
     */
    val maxConcurrentBatches: Int,
    /**
     * Fallback per-worker job limit used when no entry exists in
     * [workerLimits].
     */
    val defaultWorkerJobLimit: Int,
    /**
     * Per-worker-class overrides, keyed by simple class name. e.g.
     * `ShopifyCrawlWorker -> 2`.
     */
    val workerLimits: Map<String, Int> = emptyMap(),
    /**
     * How often the scheduler tick fires.
     */
    private val schedulerInterval: Duration,
)
