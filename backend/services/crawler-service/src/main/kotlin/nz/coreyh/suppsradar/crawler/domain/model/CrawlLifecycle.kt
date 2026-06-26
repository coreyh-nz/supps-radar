package nz.coreyh.suppsradar.crawler.domain.model

import kotlin.time.Instant

/**
 * Represents lifecycle timestamps for a crawl unit.
 */
interface CrawlLifecycle {
    /**
     * The current lifecycle state of the crawl unit.
     */
    val status: CrawlStatus

    /**
     * The moment the crawl unit began execution, or null if it has not
     * started.
     */
    val startedAt: Instant?

    /**
     * The moment the crawl unit completed successfully, or null if it has not
     * completed.
     */
    val completedAt: Instant?

    /**
     * The moment the crawl unit failed, or null if it has not failed.
     */
    val failedAt: Instant?
}
