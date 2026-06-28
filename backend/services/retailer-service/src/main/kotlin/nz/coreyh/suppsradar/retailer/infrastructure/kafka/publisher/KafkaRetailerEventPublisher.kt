package nz.coreyh.suppsradar.retailer.infrastructure.kafka.publisher

import nz.coreyh.suppsradar.events.v1.KafkaTopics
import nz.coreyh.suppsradar.events.v1.retailer.RetailerDeletedEvent
import nz.coreyh.suppsradar.events.v1.retailer.RetailerRegisteredEvent
import nz.coreyh.suppsradar.events.v1.retailer.RetailerUpdatedEvent
import nz.coreyh.suppsradar.retailer.application.publisher.RetailerEventPublisher
import nz.coreyh.suppsradar.retailer.domain.model.Retailer
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class KafkaRetailerEventPublisher(
    private val kafkaTemplate: KafkaTemplate<String, Any>,
) : RetailerEventPublisher {
    override fun publishRegistered(retailer: Retailer) {
        val event =
            RetailerRegisteredEvent(
                retailerId = retailer.id.value.value,
                platformType = retailer.platformType.name,
                apiUrl = retailer.apiUrl,
            )
        kafkaTemplate.send(
            KafkaTopics.RETAILER_EVENTS,
            retailer.id.toString(),
            event,
        )
    }

    override fun publishUpdated(retailer: Retailer) {
        val event =
            RetailerUpdatedEvent(
                retailerId = retailer.id.value.value,
                platformType = retailer.platformType.name,
                apiUrl = retailer.apiUrl,
            )
        kafkaTemplate.send(
            KafkaTopics.RETAILER_EVENTS,
            retailer.id.value.value
                .toString(),
            event,
        )
    }

    override fun publishDeleted(retailer: Retailer) {
        val event =
            RetailerDeletedEvent(
                retailerId = retailer.id.value.value,
            )
        kafkaTemplate.send(
            KafkaTopics.RETAILER_EVENTS,
            retailer.id.toString(),
            event,
        )
    }
}
