package nz.coreyh.suppsradar.crawler.infrastructure.registry

import nz.coreyh.suppsradar.crawler.domain.exception.CrawlWorkerNotFoundException
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobResult
import nz.coreyh.suppsradar.crawler.domain.registry.CrawlWorkerRegistry
import nz.coreyh.suppsradar.crawler.domain.worker.CrawlWorker
import org.springframework.stereotype.Component

@Component
class CrawlWorkerRegistryImpl(
    workers: List<CrawlWorker<out CrawlJobMetadata, out CrawlJobResult>>,
) : CrawlWorkerRegistry {
    private val registry: Map<CrawlPlatformType, CrawlWorker<out CrawlJobMetadata, out CrawlJobResult>> =
        workers.associateBy { it.retailerType }

    @Suppress("UNCHECKED_CAST")
    override fun <
        M : CrawlJobMetadata,
        R : CrawlJobResult,
    > getWorker(
        retailerType: CrawlPlatformType,
    ): CrawlWorker<M, R> =
        registry[retailerType] as? CrawlWorker<M, R>
            ?: throw CrawlWorkerNotFoundException(retailerType)

    override fun allWorkers(): Collection<CrawlWorker<out CrawlJobMetadata, out CrawlJobResult>> = registry.values
}
