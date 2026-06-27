package nz.coreyh.suppsradar.crawler.application.command.retailer

import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId

data class CreateRetailerCommand(
    val id: RetailerId,
    val platformType: CrawlPlatformType,
    val apiUrl: String,
)
