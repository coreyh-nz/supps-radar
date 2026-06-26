package nz.coreyh.suppsradar.crawler.domain.worker

import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJob
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobResult

/**
 * Base class for workers capable of processing crawl jobs for a specific
 * retailer type.
 *
 * Workers are parameterised by:
 * - M: the metadata type required to execute the job
 * - R: the result type produced after processing the job
 */
abstract class CrawlWorker<M : CrawlJobMetadata, R : CrawlJobResult>(
    val retailerType: CrawlPlatformType,
) {
    /**
     * Processes a single crawl job and returns a typed result.
     */
    abstract suspend fun processJob(
        job: CrawlJob,
        metadata: M,
    ): R
}
