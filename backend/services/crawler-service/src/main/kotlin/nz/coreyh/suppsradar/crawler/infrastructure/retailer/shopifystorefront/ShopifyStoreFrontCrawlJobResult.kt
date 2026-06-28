package nz.coreyh.suppsradar.crawler.infrastructure.retailer.shopifystorefront

import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJobResult

class ShopifyStoreFrontCrawlJobResult(
    val endCursor: String,
    val hasNextPage: Boolean,
) : CrawlJobResult
