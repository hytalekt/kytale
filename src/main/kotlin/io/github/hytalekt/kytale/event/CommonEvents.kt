package io.github.hytalekt.kytale.event

/**
 * Convenience extensions for common Hytale events.
 *
 * This file provides shortcuts for registering listeners for frequently used events.
 */

/**
 * Listens for block break events
 */
inline fun EventRegistrationBuilder.onBlockBreak(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual BlockBreakEvent type
    on<Any> { handler(it) }
}

/**
 * Listens for block place events
 */
inline fun EventRegistrationBuilder.onBlockPlace(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual BlockPlaceEvent type
    on<Any> { handler(it) }
}

/**
 * Listens for player join events
 */
inline fun EventRegistrationBuilder.onPlayerJoin(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual PlayerJoinEvent type
    on<Any> { handler(it) }
}

/**
 * Listens for player quit events
 */
inline fun EventRegistrationBuilder.onPlayerQuit(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual PlayerQuitEvent type
    on<Any> { handler(it) }
}

/**
 * Listens for player chat events
 */
inline fun EventRegistrationBuilder.onPlayerChat(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual PlayerChatEvent type
    on<Any> { handler(it) }
}

/**
 * Listens for entity damage events
 */
inline fun EventRegistrationBuilder.onEntityDamage(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual EntityDamageEvent type
    on<Any> { handler(it) }
}

/**
 * Listens for entity death events
 */
inline fun EventRegistrationBuilder.onEntityDeath(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual EntityDeathEvent type
    on<Any> { handler(it) }
}

/**
 * Listens for player interact events
 */
inline fun EventRegistrationBuilder.onPlayerInteract(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual PlayerInteractEvent type
    on<Any> { handler(it) }
}

/**
 * Listens for player move events with optional movement threshold
 */
inline fun EventRegistrationBuilder.onPlayerMove(
    minimumDistance: Double = 0.0,
    crossinline handler: (event: Any) -> Unit,
) {
    listen<Any> {
        if (minimumDistance > 0.0) {
            filter {
                // TODO: Check movement distance
                true
            }
        }
        handle { handler(it) }
    }
}

/**
 * Listens for inventory click events
 */
inline fun EventRegistrationBuilder.onInventoryClick(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual InventoryClickEvent type
    on<Any> { handler(it) }
}

/**
 * Listens for world load events
 */
inline fun EventRegistrationBuilder.onWorldLoad(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual WorldLoadEvent type
    on<Any> { handler(it) }
}

/**
 * Listens for world unload events
 */
inline fun EventRegistrationBuilder.onWorldUnload(crossinline handler: (event: Any) -> Unit) {
    // TODO: Replace Any with actual WorldUnloadEvent type
    on<Any> { handler(it) }
}
