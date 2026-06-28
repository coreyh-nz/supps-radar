package nz.coreyh.suppsradar.retailer.application.test

import nz.coreyh.suppsradar.retailer.application.command.CreateRetailerCommand
import nz.coreyh.suppsradar.retailer.application.service.RetailerService
import nz.coreyh.suppsradar.retailer.domain.model.RetailerPlatformType
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Configuration
@EnableScheduling
class SchedulerConfig

@Component
class RetailerEventTestScheduler(
    private val retailerService: RetailerService,
) {
    @Scheduled(initialDelay = 10_000, fixedDelay = 10_000)
    fun publishTestEvent() {
        retailerService.create(
            CreateRetailerCommand(
                name = "Test Retailer",
                slug = "test-retailer",
                url = "https://example.com",
                apiUrl = "https://api.example.com",
                platformType = RetailerPlatformType.SHOPIFY,
            ),
        )
    }
}
