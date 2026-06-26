package nz.coreyh.suppsradar.retailer.domain.model.crawl

/**
 * Describes the portion of a retailer's catalogue that a crawl should
 * target.
 */
sealed interface CrawlScope {
    val type: CrawlScopeType

    /**
     * Indicates that the entire retailer catalogue should be crawled.
     */
    object Full : CrawlScope {
        override val type = CrawlScopeType.FULL
    }

    /**
     * Indicates that only a specific category within the retailer catalogue
     * should be crawled.
     *
     * @property value the category identifier or slug as defined by the
     *    retailer.
     */
    data class Category(
        val value: String,
    ) : CrawlScope {
        override val type = CrawlScopeType.CATEGORY
    }
}
