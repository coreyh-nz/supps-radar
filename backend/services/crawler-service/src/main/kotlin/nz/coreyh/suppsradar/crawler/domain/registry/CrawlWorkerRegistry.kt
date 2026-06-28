package nz.coreyh.suppsradar.crawler.domain.registry

import nz.coreyh.suppsradar.crawler.domain.exception.CrawlWorkerNotFoundException
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobResult
import nz.coreyh.suppsradar.crawler.domain.worker.CrawlWorker

/**
 * Registry of crawl workers, keyed by [CrawlPlatformType].
 */
interface CrawlWorkerRegistry {
    /**
     * Returns the worker responsible for the given [CrawlPlatformType].
     *
     * @throws CrawlWorkerNotFoundException if no worker is registered for the
     *    type.
     */
    fun <M : CrawlJobMetadata, R : CrawlJobResult> getWorker(retailerType: CrawlPlatformType): CrawlWorker<M, R>

    fun allWorkers(): Collection<CrawlWorker<out CrawlJobMetadata, out CrawlJobResult>>
}
