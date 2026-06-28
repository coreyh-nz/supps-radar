package nz.coreyh.suppsradar.crawler.infrastructure.persistence.repository

import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchId
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJob
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobId
import nz.coreyh.suppsradar.crawler.infrastructure.persistence.view.PendingJobView
import org.springframework.stereotype.Repository

@Repository
class CrawlJobDispatcherRepository {
    fun findPendingJobsForDispatch(excludeBatchIds: Set<CrawlBatchId>? = null): List<PendingJobView> = emptyList()

    fun findByIdAndTryClaim(id: CrawlJobId): CrawlJob? = null
}
