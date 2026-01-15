package aster.amo.kytale.extension

import com.hypixel.hytale.logger.HytaleLogger

/**
 * Logs an info-level message using a lazy string supplier.
 *
 * The message is only constructed if info logging is enabled.
 *
 * Example:
 * ```kotlin
 * logger.info { "Player ${player.name} connected from ${player.address}" }
 * ```
 *
 * @param message lambda that produces the log message
 */
inline fun HytaleLogger.info(message: () -> String) {
    atInfo().log(message())
}

/**
 * Logs a warning-level message using a lazy string supplier.
 *
 * Example:
 * ```kotlin
 * logger.warn { "Config value out of range: $value" }
 * ```
 *
 * @param message lambda that produces the log message
 */
inline fun HytaleLogger.warn(message: () -> String) {
    atWarning().log(message())
}

/**
 * Logs an error-level message using a lazy string supplier.
 *
 * Example:
 * ```kotlin
 * logger.error { "Failed to load configuration: ${exception.message}" }
 * ```
 *
 * @param message lambda that produces the log message
 */
inline fun HytaleLogger.error(message: () -> String) {
    atSevere().log(message())
}

/**
 * Logs an error-level message with an exception.
 *
 * Example:
 * ```kotlin
 * try {
 *     riskyOperation()
 * } catch (e: Exception) {
 *     logger.error(e) { "Operation failed" }
 * }
 * ```
 *
 * @param throwable the exception to log
 * @param message lambda that produces the log message
 */
inline fun HytaleLogger.error(throwable: Throwable, message: () -> String) {
    atSevere().withCause(throwable).log(message())
}

/**
 * Logs a debug-level message using a lazy string supplier.
 *
 * Example:
 * ```kotlin
 * logger.debug { "Processing chunk at $x, $z" }
 * ```
 *
 * @param message lambda that produces the log message
 */
inline fun HytaleLogger.debug(message: () -> String) {
    atFine().log(message())
}

/**
 * Logs a trace-level message using a lazy string supplier.
 *
 * Example:
 * ```kotlin
 * logger.trace { "Entering method with args: $args" }
 * ```
 *
 * @param message lambda that produces the log message
 */
inline fun HytaleLogger.trace(message: () -> String) {
    atFinest().log(message())
}

/**
 * Logs an info-level formatted message.
 *
 * Example:
 * ```kotlin
 * logger.infoFormat("Player %s joined with %d coins", player.name, player.coins)
 * ```
 *
 * @param format the format string
 * @param arg the format argument
 */
fun HytaleLogger.infoFormat(format: String, arg: Any?) {
    atInfo().log(format, arg)
}

/**
 * Logs a warning-level formatted message.
 *
 * @param format the format string
 * @param arg the format argument
 */
fun HytaleLogger.warnFormat(format: String, arg: Any?) {
    atWarning().log(format, arg)
}

/**
 * Logs an error-level formatted message.
 *
 * @param format the format string
 * @param arg the format argument
 */
fun HytaleLogger.errorFormat(format: String, arg: Any?) {
    atSevere().log(format, arg)
}
