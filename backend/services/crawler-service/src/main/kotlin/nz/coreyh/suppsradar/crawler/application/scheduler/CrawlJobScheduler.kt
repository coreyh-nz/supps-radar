package nz.coreyh.suppsradar.crawler.application.scheduler

import nz.coreyh.suppsradar.crawler.domain.service.CrawlJobDispatcherService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CrawlJobScheduler(
    private val crawlJobDispatcherService: CrawlJobDispatcherService,
) {
    @Scheduled(fixedDelayString = $$"${crawl.executor.scheduler-interval}")
    fun tick() {
        crawlJobDispatcherService.tick()
    }
}
