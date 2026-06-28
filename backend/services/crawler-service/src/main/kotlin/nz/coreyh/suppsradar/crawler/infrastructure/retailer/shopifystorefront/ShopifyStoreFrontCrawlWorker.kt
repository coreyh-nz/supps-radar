package nz.coreyh.suppsradar.crawler.infrastructure.retailer.shopifystorefront

import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.job.CrawlJob
import nz.coreyh.suppsradar.crawler.domain.worker.CrawlWorker
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import tools.jackson.databind.JsonNode

@Component
class ShopifyStoreFrontCrawlWorker :
    CrawlWorker<
        ShopifyStoreFrontCrawlJobMetadata,
        ShopifyStoreFrontCrawlJobResult,
    >(retailerType = CrawlPlatformType.SHOPIFY) {
    override suspend fun processJob(
        job: CrawlJob,
        metadata: ShopifyStoreFrontCrawlJobMetadata,
    ): ShopifyStoreFrontCrawlJobResult {
        val response = sendQueryRequest(metadata)
        val dataNode = response.get("data")
        val productsNode = dataNode.get("products")
        val pageInfoNode = productsNode.get("pageInfo")
        return ShopifyStoreFrontCrawlJobResult(
            endCursor = pageInfoNode.get("endCursor").stringValue(),
            hasNextPage = pageInfoNode.get("hasNextPage").booleanValue(),
        )
    }

    private suspend fun sendQueryRequest(metadata: ShopifyStoreFrontCrawlJobMetadata): JsonNode =
        WebClient
            .builder()
            .baseUrl(metadata.apiUrl)
            .build()
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(
                ShopifyStoreFrontGraphQLRequest(
                    query = QUERY,
                    variables = mapOf("after" to null),
                ),
            ).retrieve()
            .awaitBody<JsonNode>()

    companion object {
        private const val QUERY = $$"""
            query Products($after: String) {
              products(
                first: 5,
                after: $after,
                query: "available_for_sale:true"
              ) {
                pageInfo {
                  endCursor
                  hasNextPage
                }
                nodes {
                  id
                  title
        
                  variants(first: 100) {
                    nodes {
                      id
                      title
                      sku
                      barcode
                      availableForSale
        
                      price {
                        amount
                        currencyCode
                      }
        
                      compareAtPrice {
                        amount
                        currencyCode
                      }
                    }
                  }
                }
              }
            }
        """
    }
}
