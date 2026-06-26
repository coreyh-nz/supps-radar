package nz.coreyh.suppsradar.retailer.application.command

import nz.coreyh.suppsradar.retailer.domain.model.RetailerPlatformType

data class CreateRetailerCommand(
    val name: String,
    val slug: String,
    val url: String,
    val apiUrl: String,
    val platformType: RetailerPlatformType,
)
