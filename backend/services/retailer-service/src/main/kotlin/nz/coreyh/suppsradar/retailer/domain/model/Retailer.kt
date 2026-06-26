package nz.coreyh.suppsradar.retailer.domain.model

import nz.coreyh.suppsradar.common.domain.model.audit.Auditable
import nz.coreyh.suppsradar.common.domain.model.audit.Deletable
import nz.coreyh.suppsradar.common.domain.model.id.Identifiable
import kotlin.time.Instant

/**
 * Represents a specific store.
 */
data class Retailer(
    override val id: RetailerId,
    val name: String,
    val slug: String,
    val url: String,
    val apiUrl: String,
    val platformType: RetailerPlatformType,
    override val createdAt: Instant,
    override val updatedAt: Instant,
    override val deletedAt: Instant?,
) : Identifiable<RetailerId>,
    Auditable,
    Deletable
