package io.github.hytalekt.kytale.event

// import com.hypixel.hytale.server.core.event.events.ecs.CancellableEcsEvent

/**
 * DSL for registering event listeners in a clean, type-safe way.
 *
 * Example usage:
 * ```
 * events {
 *     listen<PlayerJoinEvent> {
 *         priority = EventPriority.NORMAL
 *         ignoreCancelled = true
 *
 *         handle { event ->
 *             val player = event.player
 *             player.sendMessage("Welcome!")
 *         }
 *     }
 *
 *     listen<BlockBreakEvent> {
 *         filter { it.blockType == "hytale:diamond_ore" }
 *         handle { event ->
 *             event.player.sendMessage("You found diamonds!")
 *         }
 *     }
 *
 *     // Shorthand for simple listeners
 *     on<PlayerChatEvent> { event ->
 *         println("${event.player.name}: ${event.message}")
 *     }
 * }
 * ```
 */
@DslMarker
annotation class EventDsl

/**
 * Main event registration builder
 */
@EventDsl
class EventRegistrationBuilder {
    internal val listeners = mutableListOf<EventListener<*>>()

    /**
     * Registers a listener for a specific event type
     */
    inline fun <reified T : Any> listen(configure: EventListenerBuilder<T>.() -> Unit) {
        val builder = EventListenerBuilder<T>()
        builder.configure()
        addListener(builder.build())
    }

    fun <T : Any> addListener(listener: EventListener<T>) {
        listeners.add(listener)
    }

    /**
     * Shorthand for registering a simple event listener
     */
    inline fun <reified T : Any> on(noinline handler: (T) -> Unit) {
        listen<T> {
            handle(handler)
        }
    }

    /**
     * Registers multiple listeners at once
     */
    fun register() {
        // TODO: Register all listeners with the event dispatcher
    }
}

/**
 * Builder for individual event listeners
 */
@EventDsl
class EventListenerBuilder<T : Any> {
    var priority: EventPriority = EventPriority.NORMAL
    var ignoreCancelled: Boolean = false
    private var filter: ((T) -> Boolean)? = null
    private var handler: ((T) -> Unit)? = null

    /**
     * Sets a filter condition for the event
     */
    fun filter(predicate: (T) -> Boolean) {
        filter = predicate
    }

    /**
     * Sets the event handler
     */
    fun handle(block: (T) -> Unit) {
        this.handler = block
    }

    /**
     * Builds the event listener
     */
    fun build(): EventListener<T> {
        require(handler != null) { "Event handler must be set" }
        return EventListener(
            priority = priority,
            ignoreCancelled = ignoreCancelled,
            filter = filter,
            handler = handler!!,
        )
    }
}

/**
 * Represents a registered event listener
 */
data class EventListener<T : Any>(
    val priority: EventPriority,
    val ignoreCancelled: Boolean,
    val filter: ((T) -> Boolean)?,
    val handler: (T) -> Unit,
)

/**
 * Event priority levels
 */
enum class EventPriority {
    LOWEST,
    LOW,
    NORMAL,
    HIGH,
    HIGHEST,
    MONITOR,
}

/**
 * DSL function to register events
 */
fun events(configure: EventRegistrationBuilder.() -> Unit) {
    val builder = EventRegistrationBuilder()
    builder.configure()
    builder.register()
}

/**
 * Extension to cancel cancellable events
 * TODO: Uncomment when CancellableEcsEvent is properly accessible
 */
// fun CancellableEcsEvent.cancel() {
//     this.cancelled = true
// }

/**
 * Extension to check if an event is cancelled
 * TODO: Uncomment when CancellableEcsEvent is properly accessible
 */
// val CancellableEcsEvent.isCancelled: Boolean
//     get() = this.cancelled
