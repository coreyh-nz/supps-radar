package nz.coreyh.suppsradar.common.domain.exception

import nz.coreyh.suppsradar.common.domain.model.id.Id

/**
 * Thrown when a string or raw value cannot be converted into a valid [Id].
 */
class InvalidIdException(
    message: String,
) : RuntimeException(message)
