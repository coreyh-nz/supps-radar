package nz.coreyh.suppsradar.retailer.application.service

import nz.coreyh.suppsradar.common.domain.model.id.Id
import nz.coreyh.suppsradar.retailer.application.command.CreateCrawlScheduleCommand
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlSchedule
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlScheduleId
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlScope
import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlScopeType
import nz.coreyh.suppsradar.retailer.domain.repository.CrawlScheduleRepository
import org.springframework.stereotype.Service
import kotlin.time.Clock

@Service
class CrawlScheduleService(
    private val crawlScheduleRepository: CrawlScheduleRepository,
    private val clock: Clock = Clock.System,
) {
    fun create(command: CreateCrawlScheduleCommand): CrawlSchedule {
        val scope =
            when (command.scopeType) {
                CrawlScopeType.FULL -> {
                    CrawlScope.Full
                }

                CrawlScopeType.CATEGORY -> {
                    command.scopeValue?.let { CrawlScope.Category(it) }
                        ?: error("crawl scope cannot be null")
                }
            }
        val now = clock.now()
        return CrawlSchedule(
            id = CrawlScheduleId(Id.generate()),
            scope = scope,
            interval = command.interval,
            enabled = command.enabled,
            createdAt = now,
            updatedAt = now,
            deletedAt = null,
        ).also { crawlScheduleRepository.save(it) }
    }
}
