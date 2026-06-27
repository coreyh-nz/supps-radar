package nz.coreyh.suppsradar.events.v1.retailer

import kotlin.uuid.Uuid

data class RetailerRegisteredEvent(
    val retailerId: Uuid,
    val platformType: String,
    val apiUrl: String,
) : RetailerEvent
