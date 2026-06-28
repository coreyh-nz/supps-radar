package nz.coreyh.suppsradar.retailer.application.service

import nz.coreyh.suppsradar.common.domain.model.id.Id
import nz.coreyh.suppsradar.retailer.application.command.CreateRetailerCommand
import nz.coreyh.suppsradar.retailer.application.publisher.RetailerEventPublisher
import nz.coreyh.suppsradar.retailer.domain.model.Retailer
import nz.coreyh.suppsradar.retailer.domain.model.RetailerId
import nz.coreyh.suppsradar.retailer.domain.repository.RetailerRepository
import org.springframework.stereotype.Service
import kotlin.time.Clock

@Service
class RetailerService(
    private val retailerRepository: RetailerRepository,
    private val retailerEventPublisher: RetailerEventPublisher,
    private val clock: Clock = Clock.System,
) {
    fun create(command: CreateRetailerCommand): Retailer {
        val now = clock.now()
        val retailer =
            Retailer(
                id = RetailerId(Id.generate()),
                name = command.name,
                slug = command.slug,
                url = command.url,
                apiUrl = command.apiUrl,
                platformType = command.platformType,
                createdAt = now,
                updatedAt = now,
                deletedAt = null,
            )
        retailerRepository.save(retailer)
        retailerEventPublisher.publishRegistered(retailer)
        return retailer
    }
}
