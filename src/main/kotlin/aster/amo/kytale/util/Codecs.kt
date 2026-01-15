package aster.amo.kytale.util

/**
 * Utility functions for common data operations.
 *
 * Provides helper functions for working with common types
 * and data transformations in Hytale plugins.
 */

/**
 * Result wrapper for operations that can fail.
 *
 * Provides a Kotlin-idiomatic way to handle success/failure cases.
 *
 * @param T the success value type
 */
sealed class Result<out T> {
    /**
     * Represents a successful result.
     *
     * @property value the success value
     */
    data class Success<T>(val value: T) : Result<T>()

    /**
     * Represents a failed result.
     *
     * @property error the error message
     * @property cause the underlying exception, if any
     */
    data class Failure(
        val error: String,
        val cause: Throwable? = null
    ) : Result<Nothing>()

    /**
     * Returns true if this is a success result.
     */
    val isSuccess: Boolean get() = this is Success

    /**
     * Returns true if this is a failure result.
     */
    val isFailure: Boolean get() = this is Failure

    /**
     * Gets the value if successful, or null if failed.
     */
    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    /**
     * Gets the value if successful, or the default if failed.
     *
     * @param default the value to return on failure
     */
    fun getOrDefault(default: @UnsafeVariance T): T = when (this) {
        is Success -> value
        is Failure -> default
    }

    /**
     * Gets the value if successful, or throws the exception.
     *
     * @throws IllegalStateException if this is a failure
     */
    fun getOrThrow(): T = when (this) {
        is Success -> value
        is Failure -> throw IllegalStateException(error, cause)
    }

    /**
     * Maps the success value to another type.
     *
     * @param transform the transformation function
     * @return a new Result with the transformed value
     */
    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(value))
        is Failure -> this
    }

    /**
     * Flat maps the success value to another Result.
     *
     * @param transform the transformation function
     * @return the result of the transformation
     */
    inline fun <R> flatMap(transform: (T) -> Result<R>): Result<R> = when (this) {
        is Success -> transform(value)
        is Failure -> this
    }

    /**
     * Executes an action if this is a success.
     *
     * @param action the action to execute
     * @return this Result for chaining
     */
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(value)
        return this
    }

    /**
     * Executes an action if this is a failure.
     *
     * @param action the action to execute
     * @return this Result for chaining
     */
    inline fun onFailure(action: (String, Throwable?) -> Unit): Result<T> {
        if (this is Failure) action(error, cause)
        return this
    }

    companion object {
        /**
         * Creates a success result.
         *
         * @param value the success value
         */
        fun <T> success(value: T): Result<T> = Success(value)

        /**
         * Creates a failure result.
         *
         * @param error the error message
         * @param cause the underlying exception
         */
        fun failure(error: String, cause: Throwable? = null): Result<Nothing> =
            Failure(error, cause)

        /**
         * Wraps a potentially throwing operation in a Result.
         *
         * @param block the operation to execute
         * @return Success with the result, or Failure with the exception
         */
        inline fun <T> catching(block: () -> T): Result<T> {
            return try {
                Success(block())
            } catch (e: Exception) {
                Failure(e.message ?: "Unknown error", e)
            }
        }
    }
}

/**
 * Converts a nullable value to a Result.
 *
 * @param errorMessage the error message if null
 * @return Success if not null, Failure if null
 */
fun <T : Any> T?.toResult(errorMessage: String = "Value was null"): Result<T> {
    return if (this != null) {
        Result.success(this)
    } else {
        Result.failure(errorMessage)
    }
}
