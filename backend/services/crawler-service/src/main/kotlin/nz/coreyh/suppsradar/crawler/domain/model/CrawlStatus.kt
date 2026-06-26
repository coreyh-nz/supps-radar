package nz.coreyh.suppsradar.crawler.domain.model

/**
 * Represents the lifecycle state of a crawl unit.
 */
enum class CrawlStatus {
    /**
     * The crawl unit has been created but not yet running.
     */
    PENDING,

    /**
     * A worker has started processing the crawl unit.
     */
    RUNNING,

    /**
     * The crawl unit completed successfully.
     */
    COMPLETED,

    /**
     * The crawl unit has exhausted all retries or encountered a fatal error.
     */
    FAILED,
}
