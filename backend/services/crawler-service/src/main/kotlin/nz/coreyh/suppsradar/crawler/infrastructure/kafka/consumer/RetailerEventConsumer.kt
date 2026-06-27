package nz.coreyh.suppsradar.crawler.infrastructure.kafka.consumer

import io.github.oshai.kotlinlogging.KotlinLogging
import nz.coreyh.suppsradar.common.domain.model.id.Id
import nz.coreyh.suppsradar.crawler.application.command.retailer.CreateRetailerCommand
import nz.coreyh.suppsradar.crawler.application.service.retailer.RetailerService
import nz.coreyh.suppsradar.crawler.domain.model.CrawlPlatformType
import nz.coreyh.suppsradar.crawler.domain.model.retailer.RetailerId
import nz.coreyh.suppsradar.events.v1.KafkaTopics
import nz.coreyh.suppsradar.events.v1.retailer.RetailerDeletedEvent
import nz.coreyh.suppsradar.events.v1.retailer.RetailerEvent
import nz.coreyh.suppsradar.events.v1.retailer.RetailerRegisteredEvent
import nz.coreyh.suppsradar.events.v1.retailer.RetailerUpdatedEvent
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class RetailerEventConsumer(
    private val retailerService: RetailerService,
) {
    @KafkaListener(topics = [KafkaTopics.RETAILER_EVENTS])
    fun onRetailerEvent(event: RetailerEvent) {
        when (event) {
            is RetailerRegisteredEvent -> onRetailerRegistered(event)
            is RetailerUpdatedEvent -> onRetailerUpdated(event)
            is RetailerDeletedEvent -> onRetailerDeleted(event)
        }
    }

    private fun onRetailerRegistered(event: RetailerRegisteredEvent) {
        val platformType =
            runCatching { CrawlPlatformType.valueOf(event.platformType) }
                .getOrElse {
                    // TODO - implement more robust handling of invalid platform
                    logger.warn { "Unknown platform type: ${event.platformType}, skipping event" }
                    return
                }

        val command =
            CreateRetailerCommand(
                id = RetailerId(Id(event.retailerId)),
                platformType = platformType,
                apiUrl = event.apiUrl,
            )
        retailerService.create(command)
    }

    private fun onRetailerUpdated(event: RetailerUpdatedEvent) {
        logger.info { "Retailer Updated: $event" }
    }

    private fun onRetailerDeleted(event: RetailerDeletedEvent) {
        logger.info { "Retailer Deleted: $event" }
    }
}
