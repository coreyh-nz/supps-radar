package nz.coreyh.suppsradar.crawler.infrastructure.retailer.shopifystorefront

import nz.coreyh.suppsradar.crawler.domain.factory.CrawlJobFactory
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import org.springframework.stereotype.Component

@Component
class ShopifyStoreFrontCrawlJobFactory :
    CrawlJobFactory<
        ShopifyStoreFrontCrawlBatchMetadata,
        ShopifyStoreFrontCrawlJobMetadata,
        ShopifyStoreFrontCrawlJobResult,
    >(retailerType = CrawlPlatformType.SHOPIFY) {
    override fun deriveInitialJobMetadata(batchMetadata: ShopifyStoreFrontCrawlBatchMetadata): ShopifyStoreFrontCrawlJobMetadata =
        ShopifyStoreFrontCrawlJobMetadata(
            apiUrl = batchMetadata.apiUrl,
            cursor = null,
        )

    override fun deriveNextJobMetadata(
        previousMetadata: ShopifyStoreFrontCrawlJobMetadata,
        result: ShopifyStoreFrontCrawlJobResult,
    ): ShopifyStoreFrontCrawlJobMetadata? {
        if (result.hasNextPage) return null
        return ShopifyStoreFrontCrawlJobMetadata(
            apiUrl = previousMetadata.apiUrl,
            cursor = result.endCursor,
        )
    }
}
