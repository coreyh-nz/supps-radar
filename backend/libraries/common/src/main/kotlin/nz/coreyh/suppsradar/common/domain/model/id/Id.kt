package nz.coreyh.suppsradar.common.domain.model.id

import nz.coreyh.suppsradar.common.domain.exception.InvalidIdException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Strongly-typed identifier used across domain models.
 *
 * Wraps a UUIDv7 to provide type safety and to allow the underlying ID
 * strategy to evolve without affecting domain code.
 */
@JvmInline
value class Id(
    val value: Uuid,
) : DomainId {
    companion object {
        /**
         * Generates a new unique identifier using UUIDv7.
         */
        @OptIn(ExperimentalUuidApi::class)
        fun generate(): Id = Id(Uuid.generateV7())
    }

    override fun toString(): String = value.toString()
}

/**
 * Converts this string into an [Id], throwing [InvalidIdException] if the
 * value is not a valid UUID representation.
 */
fun String.toId(): Id =
    try {
        Id(Uuid.parse(this))
    } catch (e: IllegalArgumentException) {
        throw InvalidIdException("Invalid ID format: '$this'")
    }
