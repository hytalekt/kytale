/**
 * # Hexweave Events DSL
 *
 * This file provides the DSL scope for subscribing to Hytale's EventBus events.
 * Unlike ECS event systems (see [aster.amo.hexweave.dsl.systems]), EventBus events
 * are the traditional event pattern used for player actions, server events, etc.
 *
 * ## EventBus vs ECS Events
 *
 * | Aspect | EventBus Events | ECS Events |
 * |--------|-----------------|------------|
 * | Interface | [IBaseEvent], [IAsyncEvent] | [EcsEvent] |
 * | Registration | [HexweaveEventsScope] | [HexweaveSystemsScope] |
 * | Execution | Sequential by priority | ECS system ordering |
 * | Use case | Player actions, server events | Entity state changes |
 *
 * ## Registration Modes
 *
 * Hytale's EventBus supports multiple registration modes:
 *
 * | Mode | Method | Description |
 * |------|--------|-------------|
 * | Global | [listen] | Receives ALL events of a type |
 * | Keyed | [listenKeyed] | Receives events for a specific key (e.g., player UUID) |
 * | Unhandled | [listenUnhandled] | Fallback for events not handled by keyed listeners |
 * | Async | [listenAsync] | For async events with CompletableFuture |
 *
 * ## Priority Ordering
 *
 * All listeners support [EventPriority] for execution ordering:
 * - `FIRST` - Runs first (e.g., cancellation checks)
 * - `EARLY` - Runs early (e.g., validation)
 * - `NORMAL` - Default priority
 * - `LATE` - Runs late (e.g., logging)
 * - `LAST` - Runs last (e.g., cleanup)
 *
 * @see EventContext for the handler context
 * @see HexweaveSystemsScope for ECS event systems
 */
package aster.amo.hexweave.dsl.events

import aster.amo.hexweave.context.EventContext
import aster.amo.hexweave.dsl.HexweaveDsl
import aster.amo.kytale.dsl.event
import com.hypixel.hytale.event.EventPriority
import com.hypixel.hytale.event.IAsyncEvent
import com.hypixel.hytale.event.IBaseEvent
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Function

/**
 * DSL scope for subscribing to Hytale EventBus events.
 *
 * This scope provides a type-safe, Kotlin-idiomatic way to register event
 * listeners with Hytale's EventBus. All methods use reified type parameters
 * to capture event types at compile time.
 *
 * ## Registration Modes
 *
 * | Method | Use Case | Example |
 * |--------|----------|---------|
 * | [listen] | General event handling | `listen<PlayerJumpEvent> { ... }` |
 * | [listenKeyed] | Player-specific handling | `listenKeyed<ChatEvent, UUID>(playerId) { ... }` |
 * | [listenUnhandled] | Fallback/logging | `listenUnhandled<CustomEvent> { ... }` |
 * | [listenAsync] | Async event chains | `listenAsync<LoadEvent> { future -> ... }` |
 *
 * ## Priority Support
 *
 * All methods have priority variants. Priority determines execution order:
 *
 * ```kotlin
 * events {
 *     listen<PlayerChatEvent>(EventPriority.FIRST) {
 *         // Check for spam, possibly cancel
 *     }
 *
 *     listen<PlayerChatEvent>(EventPriority.NORMAL) {
 *         // Process the chat message
 *     }
 *
 *     listen<PlayerChatEvent>(EventPriority.LAST) {
 *         // Log the final message
 *     }
 * }
 * ```
 *
 * ## Filter Support
 *
 * Global listeners can include a filter predicate:
 *
 * ```kotlin
 * events {
 *     listen<DamageEvent>({ it.amount > 100 }) {
 *         // Only handles high damage events
 *         logger.warn { "Massive damage: ${event.amount}!" }
 *     }
 * }
 * ```
 *
 * ## Full Example
 *
 * ```kotlin
 * hexweave(plugin) {
 *     events {
 *         // Basic global listener
 *         listen<PlayerJumpEvent> {
 *             logger.info { "Player jumped!" }
 *         }
 *
 *         // With priority - runs early for validation
 *         listen<PlayerChatEvent>(EventPriority.EARLY) {
 *             if (event.message.contains("spam")) {
 *                 event.isCancelled = true
 *             }
 *         }
 *
 *         // With filter - only high damage
 *         listen<DamageEvent>({ it.amount > 10 }) {
 *             logger.info { "High damage: ${event.amount}" }
 *         }
 *
 *         // Keyed listener for specific player
 *         listenKeyed<PlayerChatEvent, UUID>(playerId) {
 *             // Only triggered for this specific player
 *         }
 *
 *         // Fallback for unhandled events
 *         listenUnhandled<CustomEvent> {
 *             logger.warn { "Unhandled event: $event" }
 *         }
 *
 *         // Async event handling
 *         listenAsync<AsyncLoadEvent> { future ->
 *             future.thenApply { event ->
 *                 // Process asynchronously
 *                 event
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @property plugin The plugin registering the event listeners
 *
 * @see EventContext for handler context with plugin access
 * @see EventPriority for execution ordering
 * @see IBaseEvent for synchronous events
 * @see IAsyncEvent for asynchronous events
 */
@HexweaveDsl
class HexweaveEventsScope internal constructor(
    @PublishedApi internal val plugin: JavaPlugin
) {
    // =========================================================================
    // Global Listeners
    // =========================================================================
    //
    // Global listeners receive ALL events of a given type, regardless of key.
    // Use these for general event handling that applies to any source.
    // =========================================================================

    /**
     * Subscribes to all events of a given type (global registration).
     *
     * This is the simplest listener registration. It receives every event
     * of type [T] dispatched to the EventBus with `NORMAL` priority.
     *
     * ## Usage
     *
     * ```kotlin
     * listen<PlayerJumpEvent> {
     *     // 'this' is EventContext<PlayerJumpEvent>
     *     logger.info { "Player ${event.playerRef} jumped!" }
     * }
     * ```
     *
     * ## Context
     *
     * The handler receives an [EventContext] providing:
     * - `event` - The event being processed
     * - `plugin` - The plugin instance for logging, etc.
     *
     * @param T The event type to listen for (must extend [IBaseEvent])
     * @param handler The handler function with [EventContext] as receiver
     *
     * @see listen with priority parameter for execution ordering
     * @see listen with filter for conditional handling
     */
    inline fun <reified T : IBaseEvent<*>> listen(
        noinline handler: EventContext<T>.() -> Unit
    ) {
        plugin.event<T> { event ->
            handler(EventContext(plugin, event))
        }
    }

    /**
     * Subscribes to all events of a given type with explicit priority.
     *
     * Priority controls execution order relative to other listeners for
     * the same event type. Lower priority values execute first.
     *
     * ## Priority Order
     *
     * `FIRST` → `EARLY` → `NORMAL` → `LATE` → `LAST`
     *
     * ## Usage
     *
     * ```kotlin
     * // Run early to validate/cancel before other handlers
     * listen<PlayerChatEvent>(EventPriority.EARLY) {
     *     if (event.message.isBlank()) {
     *         event.isCancelled = true
     *     }
     * }
     *
     * // Run last to log the final state
     * listen<PlayerChatEvent>(EventPriority.LAST) {
     *     logger.info { "Chat: ${event.message}" }
     * }
     * ```
     *
     * @param T The event type to listen for
     * @param priority Execution priority (`FIRST`, `EARLY`, `NORMAL`, `LATE`, `LAST`)
     * @param handler The handler function
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IBaseEvent<*>> listen(
        priority: EventPriority,
        noinline handler: EventContext<T>.() -> Unit
    ) {
        val eventClass = T::class.java as Class<IBaseEvent<Any>>
        val consumer = Consumer<IBaseEvent<Any>> { event ->
            handler(EventContext(plugin, event as T))
        }
        plugin.eventRegistry.registerGlobal(priority, eventClass, consumer)
    }

    /**
     * Subscribes to events matching a filter predicate.
     *
     * The filter is evaluated for each event before the handler is invoked.
     * Events that don't match the filter are skipped for this listener.
     *
     * ## Usage
     *
     * ```kotlin
     * // Only handle high-damage events
     * listen<DamageEvent>({ it.amount > 50 }) {
     *     logger.warn { "High damage: ${event.amount}!" }
     * }
     *
     * // Only handle admin commands
     * listen<CommandEvent>({ it.command.startsWith("/admin") }) {
     *     // Process admin command
     * }
     * ```
     *
     * ## Performance
     *
     * Filters run for every event, so keep them efficient. For complex
     * filtering, consider doing the check inside the handler instead.
     *
     * @param T The event type to listen for
     * @param filter Predicate that returns `true` to handle, `false` to skip
     * @param handler The handler function
     */
    inline fun <reified T : IBaseEvent<*>> listen(
        noinline filter: (T) -> Boolean,
        noinline handler: EventContext<T>.() -> Unit
    ) {
        plugin.event(filter) { event ->
            handler(EventContext(plugin, event))
        }
    }

    /**
     * Subscribes to events matching a filter predicate with explicit priority.
     *
     * Combines filter-based event selection with priority-based execution ordering.
     *
     * ## Usage
     *
     * ```kotlin
     * // Early validation of high-value transactions
     * listen<TransactionEvent>(EventPriority.EARLY, { it.amount > 1000 }) {
     *     logger.info { "Large transaction: ${event.amount}" }
     *     // Could add additional validation here
     * }
     * ```
     *
     * @param T The event type to listen for
     * @param priority Execution priority
     * @param filter Predicate that returns `true` to handle, `false` to skip
     * @param handler The handler function
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IBaseEvent<*>> listen(
        priority: EventPriority,
        noinline filter: (T) -> Boolean,
        noinline handler: EventContext<T>.() -> Unit
    ) {
        val eventClass = T::class.java as Class<IBaseEvent<Any>>
        val consumer = Consumer<IBaseEvent<Any>> { event ->
            val typed = event as T
            if (filter(typed)) {
                handler(EventContext(plugin, typed))
            }
        }
        plugin.eventRegistry.registerGlobal(priority, eventClass, consumer)
    }

    // =========================================================================
    // Keyed Listeners
    // =========================================================================
    //
    // Keyed listeners receive events ONLY for a specific key. This is how
    // Hytale routes events to the correct handlers - e.g., player events
    // are keyed by player UUID, so you can listen for a specific player.
    // =========================================================================

    /**
     * Subscribes to events for a specific key.
     *
     * Keyed listeners only receive events dispatched with a matching key.
     * This is the primary way to handle player-specific events, where events
     * are keyed by player UUID.
     *
     * ## How Keyed Events Work
     *
     * When an event is dispatched, Hytale routes it based on its key:
     * 1. Keyed listeners for that key receive it first
     * 2. If no keyed listener handles it, unhandled listeners receive it
     * 3. Global listeners receive all events regardless of key
     *
     * ## Usage
     *
     * ```kotlin
     * // Listen for a specific player's chat messages
     * listenKeyed<PlayerChatEvent, UUID>(targetPlayerId) {
     *     logger.info { "Target player said: ${event.message}" }
     * }
     *
     * // Track specific entity actions
     * listenKeyed<EntityActionEvent, EntityId>(bossEntityId) {
     *     // Handle boss-specific actions
     * }
     * ```
     *
     * ## Type Parameters
     *
     * - `T` - The event type (must be keyed by type `K`)
     * - `K` - The key type (e.g., `UUID` for player events)
     *
     * @param T The event type (extends [IBaseEvent] with key type `K`)
     * @param K The key type
     * @param key The specific key to listen for (e.g., player UUID)
     * @param handler The handler function
     *
     * @see listenUnhandled for fallback handling when no keyed listener matches
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IBaseEvent<K>, K : Any> listenKeyed(
        key: K,
        noinline handler: EventContext<T>.() -> Unit
    ) {
        val eventClass = T::class.java as Class<in IBaseEvent<K>>
        val consumer = Consumer<IBaseEvent<K>> { event ->
            handler(EventContext(plugin, event as T))
        }
        plugin.eventRegistry.register(eventClass, key, consumer)
    }

    /**
     * Subscribes to events for a specific key with explicit priority.
     *
     * Combines keyed event routing with priority-based execution ordering.
     *
     * ## Usage
     *
     * ```kotlin
     * // Early handling for VIP player
     * listenKeyed<PlayerChatEvent, UUID>(vipPlayerId, EventPriority.EARLY) {
     *     // VIP gets priority processing
     *     event.message = "[VIP] ${event.message}"
     * }
     * ```
     *
     * @param T The event type
     * @param K The key type
     * @param key The specific key to listen for
     * @param priority Execution priority
     * @param handler The handler function
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IBaseEvent<K>, K : Any> listenKeyed(
        key: K,
        priority: EventPriority,
        noinline handler: EventContext<T>.() -> Unit
    ) {
        val eventClass = T::class.java as Class<in IBaseEvent<K>>
        val consumer = Consumer<IBaseEvent<K>> { event ->
            handler(EventContext(plugin, event as T))
        }
        plugin.eventRegistry.register(priority, eventClass, key, consumer)
    }

    // =========================================================================
    // Unhandled Listeners (Fallback)
    // =========================================================================
    //
    // Unhandled listeners are invoked when an event has a key but no keyed
    // listener was registered for that key. They act as a fallback/default.
    // Global listeners still receive ALL events regardless of this mechanism.
    // =========================================================================

    /**
     * Subscribes to unhandled events (fallback handler).
     *
     * Unhandled listeners are called when:
     * 1. An event is dispatched with a key
     * 2. No keyed listener is registered for that key
     *
     * This is useful for providing default behavior or logging events that
     * weren't explicitly handled.
     *
     * ## When to Use
     *
     * - Logging events that slipped through
     * - Default handling for new/unknown keys
     * - Debugging event routing issues
     * - Catch-all error handling
     *
     * ## Usage
     *
     * ```kotlin
     * // Log any unhandled chat events
     * listenUnhandled<PlayerChatEvent> {
     *     logger.warn { "Unhandled chat from ${event.playerRef}: ${event.message}" }
     * }
     *
     * // Default behavior for unregistered commands
     * listenUnhandled<CommandEvent> {
     *     event.playerRef.sendMessage("Unknown command: ${event.command}")
     * }
     * ```
     *
     * ## Note
     *
     * Global listeners ([listen]) receive ALL events regardless of whether
     * they're "handled" or not. Unhandled listeners are specifically for
     * events that had a key but no matching keyed listener.
     *
     * @param T The event type to listen for
     * @param handler The handler function
     *
     * @see listenKeyed for registering keyed listeners
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IBaseEvent<*>> listenUnhandled(
        noinline handler: EventContext<T>.() -> Unit
    ) {
        val eventClass = T::class.java as Class<IBaseEvent<Any>>
        val consumer = Consumer<IBaseEvent<Any>> { event ->
            handler(EventContext(plugin, event as T))
        }
        plugin.eventRegistry.registerUnhandled(eventClass, consumer)
    }

    /**
     * Subscribes to unhandled events with explicit priority.
     *
     * ## Usage
     *
     * ```kotlin
     * // Last-resort error logging
     * listenUnhandled<ErrorEvent>(EventPriority.LAST) {
     *     logger.error { "Unhandled error: ${event.error}" }
     * }
     * ```
     *
     * @param T The event type to listen for
     * @param priority Execution priority
     * @param handler The handler function
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IBaseEvent<*>> listenUnhandled(
        priority: EventPriority,
        noinline handler: EventContext<T>.() -> Unit
    ) {
        val eventClass = T::class.java as Class<IBaseEvent<Any>>
        val consumer = Consumer<IBaseEvent<Any>> { event ->
            handler(EventContext(plugin, event as T))
        }
        plugin.eventRegistry.registerUnhandled(priority, eventClass, consumer)
    }

    // =========================================================================
    // Async Listeners
    // =========================================================================
    //
    // Async listeners handle events that support asynchronous processing.
    // These events implement IAsyncEvent and use CompletableFuture for
    // non-blocking operations like database queries or network calls.
    // =========================================================================

    /**
     * Subscribes to async events with [CompletableFuture] support.
     *
     * Async listeners receive a [CompletableFuture] wrapping the event and
     * return a [CompletableFuture] with the (potentially modified) event.
     * This enables non-blocking operations like database queries or API calls.
     *
     * ## How Async Events Work
     *
     * 1. Event is dispatched wrapped in a CompletableFuture
     * 2. Listeners can chain async operations (thenApply, thenCompose, etc.)
     * 3. The final future is awaited before the event completes
     * 4. Each listener sees the result of previous listeners
     *
     * ## Usage
     *
     * ```kotlin
     * // Async data loading
     * listenAsync<PlayerLoadEvent> { future ->
     *     future.thenApply { event ->
     *         // Load player data asynchronously
     *         event.playerData = loadPlayerData(event.playerId)
     *         event
     *     }
     * }
     *
     * // Chain multiple async operations
     * listenAsync<WorldLoadEvent> { future ->
     *     future
     *         .thenCompose { event ->
     *             loadWorldConfig(event.worldId).thenApply { config ->
     *                 event.apply { this.config = config }
     *             }
     *         }
     *         .thenApply { event ->
     *             // Additional processing
     *             event
     *         }
     * }
     * ```
     *
     * ## Important
     *
     * - Always return a CompletableFuture (even if just returning the input)
     * - Don't block the future - use thenApply/thenCompose for chaining
     * - Handle exceptions in the future chain
     *
     * @param T The async event type (must extend [IAsyncEvent])
     * @param handler Function that takes and returns a CompletableFuture
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IAsyncEvent<*>> listenAsync(
        noinline handler: (CompletableFuture<T>) -> CompletableFuture<T>
    ) {
        val eventClass = T::class.java as Class<IAsyncEvent<Any>>
        val function = Function<CompletableFuture<IAsyncEvent<Any>>, CompletableFuture<IAsyncEvent<Any>>> { future ->
            @Suppress("UNCHECKED_CAST")
            handler(future as CompletableFuture<T>) as CompletableFuture<IAsyncEvent<Any>>
        }
        plugin.eventRegistry.registerAsyncGlobal(eventClass, function)
    }

    /**
     * Subscribes to async events with explicit priority.
     *
     * Priority determines the order in which async listeners process the
     * future chain. Earlier priorities see the event first.
     *
     * ## Usage
     *
     * ```kotlin
     * // Early validation before other async processing
     * listenAsync<AsyncLoginEvent>(EventPriority.EARLY) { future ->
     *     future.thenApply { event ->
     *         if (!validateCredentials(event.credentials)) {
     *             event.isCancelled = true
     *         }
     *         event
     *     }
     * }
     *
     * // Late logging after all processing
     * listenAsync<AsyncLoginEvent>(EventPriority.LATE) { future ->
     *     future.thenApply { event ->
     *         logger.info { "Login ${if (event.isCancelled) "rejected" else "accepted"}" }
     *         event
     *     }
     * }
     * ```
     *
     * @param T The async event type
     * @param priority Execution priority
     * @param handler Function that takes and returns a CompletableFuture
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : IAsyncEvent<*>> listenAsync(
        priority: EventPriority,
        noinline handler: (CompletableFuture<T>) -> CompletableFuture<T>
    ) {
        val eventClass = T::class.java as Class<IAsyncEvent<Any>>
        val function = Function<CompletableFuture<IAsyncEvent<Any>>, CompletableFuture<IAsyncEvent<Any>>> { future ->
            @Suppress("UNCHECKED_CAST")
            handler(future as CompletableFuture<T>) as CompletableFuture<IAsyncEvent<Any>>
        }
        plugin.eventRegistry.registerAsyncGlobal(priority, eventClass, function)
    }
}
