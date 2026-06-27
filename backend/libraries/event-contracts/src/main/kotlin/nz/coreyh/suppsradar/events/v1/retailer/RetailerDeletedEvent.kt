package nz.coreyh.suppsradar.events.v1.retailer

import kotlin.uuid.Uuid

data class RetailerDeletedEvent(
    val retailerId: Uuid,
) : RetailerEvent
