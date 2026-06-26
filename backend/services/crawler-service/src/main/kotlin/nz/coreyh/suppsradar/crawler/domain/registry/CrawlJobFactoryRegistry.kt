package nz.coreyh.suppsradar.crawler.domain.registry

import nz.coreyh.suppsradar.crawler.domain.exception.CrawlJobFactoryNotFoundException
import nz.coreyh.suppsradar.crawler.domain.factory.CrawlJobFactory
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobResult

/**
 * Registry of crawl job factories, keyed by [CrawlPlatformType].
 */
interface CrawlJobFactoryRegistry {
    /**
     * Returns the factory responsible for the given [CrawlPlatformType].
     *
     * @throws CrawlJobFactoryNotFoundException if no job factory is registered for the type.
     */
    fun <
        BM : CrawlBatchMetadata,
        JM : CrawlJobMetadata,
        R : CrawlJobResult,
    > getFactory(
        retailerType: CrawlPlatformType,
    ): CrawlJobFactory<BM, JM, R>
}
