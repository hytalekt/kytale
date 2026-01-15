package aster.amo.kytale.util

import java.util.UUID

/**
 * Validation utilities for input validation and argument checking.
 *
 * These utilities provide fluent validation patterns for common game plugin scenarios.
 */

/**
 * Validates a condition and returns a Result.
 *
 * Example:
 * ```kotlin
 * validate(amount > 0) { "Amount must be positive" }
 *     .flatMap { processPayment(amount) }
 * ```
 *
 * @param condition the condition to validate
 * @param errorMessage the error message if validation fails
 * @return Success if valid, Failure otherwise
 */
inline fun validate(condition: Boolean, errorMessage: () -> String): Result<Unit> {
    return if (condition) {
        Result.success(Unit)
    } else {
        Result.failure(errorMessage())
    }
}

/**
 * Validates that a value is not null.
 *
 * @param value the value to check
 * @param errorMessage the error message if null
 * @return Success with the non-null value, or Failure
 */
inline fun <T : Any> validateNotNull(value: T?, errorMessage: () -> String): Result<T> {
    return if (value != null) {
        Result.success(value)
    } else {
        Result.failure(errorMessage())
    }
}

/**
 * Validation builder for fluent validation chains.
 *
 * Example:
 * ```kotlin
 * val result = ValidationBuilder<PlayerData>()
 *     .require(data.name.isNotBlank()) { "Name required" }
 *     .require(data.level >= 1) { "Level must be at least 1" }
 *     .require(data.health > 0) { "Health must be positive" }
 *     .build { data }
 * ```
 */
class ValidationBuilder<T> {
    @PublishedApi
    internal val errors = mutableListOf<String>()

    /**
     * Adds a validation requirement.
     *
     * @param condition the condition that must be true
     * @param errorMessage the error message if condition is false
     */
    fun require(condition: Boolean, errorMessage: () -> String): ValidationBuilder<T> {
        if (!condition) {
            errors.add(errorMessage())
        }
        return this
    }

    /**
     * Adds a validation requirement with a value check.
     *
     * @param value the value to check
     * @param predicate the validation predicate
     * @param errorMessage the error message if validation fails
     */
    inline fun <V> requireThat(
        value: V,
        predicate: (V) -> Boolean,
        errorMessage: () -> String
    ): ValidationBuilder<T> {
        if (!predicate(value)) {
            errors.add(errorMessage())
        }
        return this
    }

    /**
     * Builds the validation result.
     *
     * @param valueProvider function to provide the validated value
     * @return Success with the value if all validations passed, Failure with errors otherwise
     */
    inline fun build(valueProvider: () -> T): Result<T> {
        return if (errors.isEmpty()) {
            Result.success(valueProvider())
        } else {
            Result.failure(errors.joinToString("; "))
        }
    }

    /**
     * Returns all validation errors.
     */
    fun errors(): List<String> = errors.toList()

    /**
     * Returns true if validation passed.
     */
    fun isValid(): Boolean = errors.isEmpty()
}

/**
 * Creates a validation builder.
 */
fun <T> validation(): ValidationBuilder<T> = ValidationBuilder()

/**
 * String validation extensions.
 */

/**
 * Validates that this string is not blank.
 *
 * @param fieldName the name of the field for error messages
 * @return Success with the string, or Failure
 */
fun String.validateNotBlank(fieldName: String = "Value"): Result<String> {
    return if (isNotBlank()) {
        Result.success(this)
    } else {
        Result.failure("$fieldName cannot be blank")
    }
}

/**
 * Validates string length is within range.
 *
 * @param min minimum length (inclusive)
 * @param max maximum length (inclusive)
 * @param fieldName the name of the field for error messages
 */
fun String.validateLength(min: Int, max: Int, fieldName: String = "Value"): Result<String> {
    return when {
        length < min -> Result.failure("$fieldName must be at least $min characters")
        length > max -> Result.failure("$fieldName must be at most $max characters")
        else -> Result.success(this)
    }
}

/**
 * Validates string matches a regex pattern.
 *
 * @param pattern the regex pattern
 * @param errorMessage the error message if pattern doesn't match
 */
fun String.validatePattern(pattern: Regex, errorMessage: String): Result<String> {
    return if (matches(pattern)) {
        Result.success(this)
    } else {
        Result.failure(errorMessage)
    }
}

/**
 * Validates string is alphanumeric.
 *
 * @param fieldName the name of the field for error messages
 */
fun String.validateAlphanumeric(fieldName: String = "Value"): Result<String> {
    return if (all { it.isLetterOrDigit() }) {
        Result.success(this)
    } else {
        Result.failure("$fieldName must be alphanumeric")
    }
}

/**
 * Number validation extensions.
 */

/**
 * Validates number is within range.
 *
 * @param min minimum value (inclusive)
 * @param max maximum value (inclusive)
 * @param fieldName the name of the field for error messages
 */
fun Int.validateRange(min: Int, max: Int, fieldName: String = "Value"): Result<Int> {
    return when {
        this < min -> Result.failure("$fieldName must be at least $min")
        this > max -> Result.failure("$fieldName must be at most $max")
        else -> Result.success(this)
    }
}

/**
 * Validates number is positive.
 *
 * @param fieldName the name of the field for error messages
 */
fun Int.validatePositive(fieldName: String = "Value"): Result<Int> {
    return if (this > 0) {
        Result.success(this)
    } else {
        Result.failure("$fieldName must be positive")
    }
}

/**
 * Validates number is non-negative.
 *
 * @param fieldName the name of the field for error messages
 */
fun Int.validateNonNegative(fieldName: String = "Value"): Result<Int> {
    return if (this >= 0) {
        Result.success(this)
    } else {
        Result.failure("$fieldName cannot be negative")
    }
}

/**
 * Validates number is within range.
 */
fun Double.validateRange(min: Double, max: Double, fieldName: String = "Value"): Result<Double> {
    return when {
        this < min -> Result.failure("$fieldName must be at least $min")
        this > max -> Result.failure("$fieldName must be at most $max")
        else -> Result.success(this)
    }
}

/**
 * Validates number is positive.
 */
fun Double.validatePositive(fieldName: String = "Value"): Result<Double> {
    return if (this > 0) {
        Result.success(this)
    } else {
        Result.failure("$fieldName must be positive")
    }
}

/**
 * UUID validation.
 */

/**
 * Parses and validates a UUID string.
 *
 * @return Success with the UUID, or Failure if invalid
 */
fun String.validateUUID(): Result<UUID> {
    return try {
        Result.success(UUID.fromString(this))
    } catch (e: IllegalArgumentException) {
        Result.failure("Invalid UUID format")
    }
}

/**
 * Collection validation.
 */

/**
 * Validates collection is not empty.
 *
 * @param fieldName the name of the field for error messages
 */
fun <T> Collection<T>.validateNotEmpty(fieldName: String = "Collection"): Result<Collection<T>> {
    return if (isNotEmpty()) {
        Result.success(this)
    } else {
        Result.failure("$fieldName cannot be empty")
    }
}

/**
 * Validates collection size is within range.
 *
 * @param min minimum size (inclusive)
 * @param max maximum size (inclusive)
 * @param fieldName the name of the field for error messages
 */
fun <T> Collection<T>.validateSize(min: Int, max: Int, fieldName: String = "Collection"): Result<Collection<T>> {
    return when {
        size < min -> Result.failure("$fieldName must have at least $min items")
        size > max -> Result.failure("$fieldName must have at most $max items")
        else -> Result.success(this)
    }
}

/**
 * Common validation patterns.
 */
object Patterns {
    val USERNAME = Regex("^[a-zA-Z0-9_]{3,16}$")
    val EMAIL = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
    val ALPHANUMERIC = Regex("^[a-zA-Z0-9]+$")
    val NUMERIC = Regex("^[0-9]+$")
    val HEX_COLOR = Regex("^#[0-9A-Fa-f]{6}$")
}

/**
 * Validates a username format.
 *
 * @return Success with the username, or Failure if invalid
 */
fun String.validateUsername(): Result<String> {
    return validatePattern(Patterns.USERNAME, "Username must be 3-16 alphanumeric characters or underscores")
}

/**
 * Validates an email format.
 *
 * @return Success with the email, or Failure if invalid
 */
fun String.validateEmail(): Result<String> {
    return validatePattern(Patterns.EMAIL, "Invalid email format")
}
