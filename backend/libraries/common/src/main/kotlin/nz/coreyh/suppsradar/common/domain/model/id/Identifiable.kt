package nz.coreyh.suppsradar.common.domain.model.id

/**
 * Represents a domain model that exposes a strongly-typed identifier.
 *
 * The generic [ID] type must implement [DomainId], ensuring type safety
 * across modules and preventing accidental mixing of identifiers.
 */
interface Identifiable<ID : DomainId> {
    val id: ID
}
