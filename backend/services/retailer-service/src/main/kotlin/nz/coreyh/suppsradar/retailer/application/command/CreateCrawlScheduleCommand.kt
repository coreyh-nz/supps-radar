package nz.coreyh.suppsradar.retailer.application.command

import nz.coreyh.suppsradar.retailer.domain.model.crawl.CrawlScopeType
import kotlin.time.Duration

data class CreateCrawlScheduleCommand(
    val scopeType: CrawlScopeType,
    val scopeValue: String?,
    val interval: Duration,
    val enabled: Boolean,
)
