package nz.coreyh.suppsradar.crawler.infrastructure.retailer.shopifystorefront

import nz.coreyh.suppsradar.crawler.domain.model.batch.CrawlBatchMetadata

data class ShopifyStoreFrontCrawlBatchMetadata(
    val apiUrl: String,
) : CrawlBatchMetadata
