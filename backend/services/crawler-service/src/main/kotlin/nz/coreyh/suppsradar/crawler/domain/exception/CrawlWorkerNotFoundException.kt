package nz.coreyh.suppsradar.crawler.domain.exception

import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.worker.CrawlWorker

/**
 * Thrown when no [CrawlWorker] is registered for the requested
 * [CrawlPlatformType].
 */
class CrawlWorkerNotFoundException(
    retailerType: CrawlPlatformType,
) : RuntimeException(
        "No CrawlWorker registered for retailer type: $retailerType",
    )
