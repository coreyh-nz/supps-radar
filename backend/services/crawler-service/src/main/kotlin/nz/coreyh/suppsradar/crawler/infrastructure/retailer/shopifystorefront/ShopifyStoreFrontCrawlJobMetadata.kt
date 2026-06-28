package nz.coreyh.suppsradar.crawler.infrastructure.retailer.shopifystorefront

import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobMetadata

data class ShopifyStoreFrontCrawlJobMetadata(
    val apiUrl: String,
    val cursor: String?,
) : CrawlJobMetadata
