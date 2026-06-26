package nz.coreyh.suppsradar.crawler.infrastructure.registry

import nz.coreyh.suppsradar.crawler.domain.exception.CrawlWorkerNotFoundException
import nz.coreyh.suppsradar.crawler.domain.factory.CrawlJobFactory
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobResult
import nz.coreyh.suppsradar.crawler.domain.registry.CrawlJobFactoryRegistry
import org.springframework.stereotype.Component

@Component
class CrawlJobFactoryRegistryImpl(
    factories: List<CrawlJobFactory<*, *, *>>,
) : CrawlJobFactoryRegistry {
    private val registry: Map<CrawlPlatformType, CrawlJobFactory<*, *, *>> =
        factories.associateBy { it.retailerType }

    @Suppress("UNCHECKED_CAST")
    override fun <
        BM : CrawlBatchMetadata,
        JM : CrawlJobMetadata,
        R : CrawlJobResult,
    > getFactory(
        retailerType: CrawlPlatformType,
    ): CrawlJobFactory<BM, JM, R> =
        registry[retailerType] as? CrawlJobFactory<BM, JM, R>
            ?: throw CrawlWorkerNotFoundException(retailerType)
}
