package nz.coreyh.suppsradar.crawler.application.service.retailer

import nz.coreyh.suppsradar.crawler.application.command.retailer.CreateRetailerCommand
import nz.coreyh.suppsradar.crawler.domain.model.retailer.Retailer
import nz.coreyh.suppsradar.crawler.domain.repository.retailer.RetailerRepository
import org.springframework.stereotype.Service
import kotlin.time.Clock

@Service
class RetailerService(
    private val retailerRepository: RetailerRepository,
    private val clock: Clock = Clock.System,
) {
    fun create(command: CreateRetailerCommand): Retailer {
        val now = clock.now()
        return Retailer(
            id = command.id,
            platformType = command.platformType,
            apiUrl = command.apiUrl,
            createdAt = now,
            updatedAt = now,
        ).also { retailerRepository.save(it) }
    }
}
