package io.github.hytalekt.kytale.ext

import com.google.common.flogger.AbstractLogger
import com.google.common.flogger.LoggingApi
import java.util.logging.Level

/**
 * Returns a logging API for the specified level, or null if disabled
 *
 * @param level The logging level to check
 * @return The logging API if enabled, null otherwise
 */
fun <T : AbstractLogger<Api>, Api : LoggingApi<Api>> T.atLevelOrNull(level: Level): LoggingApi<Api>? = at(level).takeIf { it.isEnabled }

/** Returns a SEVERE level logging API, or null if disabled */
fun <T : AbstractLogger<Api>, Api : LoggingApi<Api>> T.atSevereOrNull(): LoggingApi<Api>? = atLevelOrNull(Level.SEVERE)

/** Returns a WARNING level logging API, or null if disabled */
fun <T : AbstractLogger<Api>, Api : LoggingApi<Api>> T.atWarningOrNull(): LoggingApi<Api>? = atLevelOrNull(Level.WARNING)

/** Returns an INFO level logging API, or null if disabled */
fun <T : AbstractLogger<Api>, Api : LoggingApi<Api>> T.atInfoOrNull(): LoggingApi<Api>? = atLevelOrNull(Level.INFO)

/** Returns a CONFIG level logging API, or null if disabled */
fun <T : AbstractLogger<Api>, Api : LoggingApi<Api>> T.atConfigOrNull(): LoggingApi<Api>? = atLevelOrNull(Level.CONFIG)

/** Returns a FINE level logging API, or null if disabled */
fun <T : AbstractLogger<Api>, Api : LoggingApi<Api>> T.atFineOrNull(): LoggingApi<Api>? = atLevelOrNull(Level.FINE)

/** Returns a FINER level logging API, or null if disabled */
fun <T : AbstractLogger<Api>, Api : LoggingApi<Api>> T.atFinerOrNull(): LoggingApi<Api>? = atLevelOrNull(Level.FINER)

/** Returns a FINEST level logging API, or null if disabled */
fun <T : AbstractLogger<Api>, Api : LoggingApi<Api>> T.atFinestOrNull(): LoggingApi<Api>? = atLevelOrNull(Level.FINEST)
