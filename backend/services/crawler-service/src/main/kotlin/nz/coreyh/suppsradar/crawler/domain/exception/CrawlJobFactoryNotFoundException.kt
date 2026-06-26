package nz.coreyh.suppsradar.crawler.domain.exception

import nz.coreyh.suppsradar.crawler.domain.factory.CrawlJobFactory
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType

/**
 * Thrown when no [CrawlJobFactory] is registered for the requested
 * [CrawlPlatformType].
 */
class CrawlJobFactoryNotFoundException(
    retailerType: CrawlPlatformType,
) : RuntimeException(
        "No CrawlJobFactory registered for retailer type: $retailerType",
    )
