package nz.coreyh.suppsradar.crawler.domain.factory

import nz.coreyh.suppsradar.common.domain.model.id.Id
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.CrawlStatus
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId
import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJob
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobId
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobMetadata
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobResult
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId
import kotlin.time.Instant

/**
 * Defines how crawl jobs are derived for a specific job type.
 *
 * Each job type provides its own implementation to express how
 * pagination or continuation works for that specific job/result shape.
 *
 * @param BM the batch metadata type for this job type
 * @param JM the job metadata type for this job type
 * @param R the job result type produced by the worker
 */
abstract class CrawlJobFactory<
    BM : CrawlBatchMetadata,
    JM : CrawlJobMetadata,
    R : CrawlJobResult,
>(
    val retailerType: CrawlPlatformType,
) {
    /**
     * Derives the initial job metadata from the batch metadata.
     *
     * This is called when a new batch is created and determines the
     * starting point for the crawl (e.g., initial cursor, initial URL).
     *
     * @return the metadata for the first job in the batch
     */
    abstract fun deriveInitialJobMetadata(batchMetadata: BM): JM

    /**
     * Derives the metadata for the next job based on the result of a
     * completed job.
     *
     * If the result indicates that no further work is required, this
     * method returns `null`to signal that the batch has no more jobs to
     * enqueue.
     *
     * @return metadata for the next job, or `null` if the crawl is
     *         complete
     */
    abstract fun deriveNextJobMetadata(
        previousMetadata: JM,
        result: R,
    ): JM?
}

/**
 * Creates the first job for a newly created batch by deriving the
 * initial job metadata.
 *
 * @return a new [CrawlJob] representing the first step in the batch
 */
fun <
    BM : CrawlBatchMetadata,
    JM : CrawlJobMetadata,
    R : CrawlJobResult,
> CrawlJobFactory<BM, JM, R>.createInitialJob(
    batchId: CrawlBatchId,
    retailerId: RetailerId,
    batchMetadata: BM,
    now: Instant,
) = createJob(
    batchId = batchId,
    retailerId = retailerId,
    metadata = deriveInitialJobMetadata(batchMetadata),
    now = now,
)

/**
 * Creates the next job in the batch by deriving the next job metadata
 * from the completed job's result. If no further work is required,
 * returns `null`.
 *
 * @return a new [CrawlJob] representing the next step in the batch,
 *         or `null` if the crawl is complete
 */
@Suppress("UNCHECKED_CAST")
fun <
    BM : CrawlBatchMetadata,
    JM : CrawlJobMetadata,
    R : CrawlJobResult,
> CrawlJobFactory<BM, JM, R>.createNextJob(
    previousJob: CrawlJob,
    result: R,
    now: Instant,
): CrawlJob? {
    val previousMetadata = previousJob.metadata as JM
    val nextMetadata = deriveNextJobMetadata(previousMetadata, result) ?: return null
    return createJob(
        batchId = previousJob.batchId,
        retailerId = previousJob.retailerId,
        metadata = nextMetadata,
        now = now,
    )
}

/**
 * Constructs a new [CrawlJob] with the correct identifiers, lifecycle
 * defaults, and metadata for the given batch and retailer.
 *
 * @return a fully initialized [CrawlJob] ready to be persisted or
 *         enqueued
 */
private fun <JM : CrawlJobMetadata> createJob(
    batchId: CrawlBatchId,
    retailerId: RetailerId,
    metadata: JM,
    now: Instant,
) = CrawlJob(
    id = CrawlJobId(Id.generate()),
    batchId = batchId,
    retailerId = retailerId,
    attempts = 0,
    maxAttempts = 3,
    error = null,
    metadata = metadata,
    status = CrawlStatus.PENDING,
    startedAt = null,
    completedAt = null,
    failedAt = null,
    createdAt = now,
    updatedAt = now,
    deletedAt = null,
)
