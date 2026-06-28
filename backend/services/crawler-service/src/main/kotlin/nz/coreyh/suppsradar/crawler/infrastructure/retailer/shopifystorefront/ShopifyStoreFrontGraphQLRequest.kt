package nz.coreyh.suppsradar.crawler.infrastructure.retailer.shopifystorefront

data class ShopifyStoreFrontGraphQLRequest(
    val query: String,
    val variables: Map<String, Any?>,
)
