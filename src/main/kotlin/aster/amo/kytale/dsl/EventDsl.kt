package aster.amo.kytale.dsl

import com.hypixel.hytale.event.IBaseEvent
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import java.util.function.Consumer

/**
 * Registers a global event listener for the specified event type.
 *
 * Uses reified type parameters to eliminate the need for passing
 * the event class explicitly.
 *
 * Example:
 * ```kotlin
 * event<PlayerConnectEvent> { event ->
 *     logger.info { "Player connected!" }
 * }
 * ```
 *
 * @param T the event type to listen for (must extend IBaseEvent)
 * @param handler the callback invoked when the event fires
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : IBaseEvent<*>> JavaPlugin.event(noinline handler: (T) -> Unit) {
    val eventClass = T::class.java as Class<IBaseEvent<Any>>
    val consumer = Consumer<IBaseEvent<Any>> { event -> handler(event as T) }
    eventRegistry.registerGlobal(eventClass, consumer)
}

/**
 * Registers a filtered global event listener.
 *
 * Only invokes the handler when the predicate returns true.
 *
 * Example:
 * ```kotlin
 * event<PlayerChatEvent>(
 *     filter = { it.message.startsWith("!") }
 * ) { event ->
 *     handleCommand(event)
 * }
 * ```
 *
 * @param T the event type to listen for
 * @param filter predicate that must return true for the handler to be called
 * @param handler the callback invoked when the event fires and passes the filter
 */
@Suppress("UNCHECKED_CAST")
inline fun <reified T : IBaseEvent<*>> JavaPlugin.event(
    noinline filter: (T) -> Boolean,
    noinline handler: (T) -> Unit
) {
    val eventClass = T::class.java as Class<IBaseEvent<Any>>
    val consumer = Consumer<IBaseEvent<Any>> { event ->
        val typedEvent = event as T
        if (filter(typedEvent)) {
            handler(typedEvent)
        }
    }
    eventRegistry.registerGlobal(eventClass, consumer)
}

/**
 * Registers multiple event listeners using a builder DSL.
 *
 * Example:
 * ```kotlin
 * events {
 *     on<PlayerConnectEvent> { logger.info { "Player connected" } }
 *     on<PlayerDisconnectEvent> { logger.info { "Player disconnected" } }
 * }
 * ```
 *
 * @param block the builder block containing event registrations
 */
inline fun JavaPlugin.events(block: EventRegistration.() -> Unit) {
    EventRegistration(this).apply(block)
}

/**
 * Builder class for registering multiple events.
 *
 * @property plugin the plugin to register events for
 */
class EventRegistration(
    @PublishedApi internal val plugin: JavaPlugin
) {
    /**
     * Registers a listener for the specified event type.
     *
     * @param T the event type
     * @param handler the event callback
     */
    inline fun <reified T : IBaseEvent<*>> on(noinline handler: (T) -> Unit) {
        plugin.event(handler)
    }

    /**
     * Registers a filtered listener for the specified event type.
     *
     * @param T the event type
     * @param filter predicate that must return true for the handler to be called
     * @param handler the event callback
     */
    inline fun <reified T : IBaseEvent<*>> on(
        noinline filter: (T) -> Boolean,
        noinline handler: (T) -> Unit
    ) {
        plugin.event(filter, handler)
    }
}

/**
 * Creates a typed event handler that can be stored and registered later.
 *
 * Example:
 * ```kotlin
 * val handler = eventHandler<PlayerConnectEvent> {
 *     logger.info { "Welcome!" }
 * }
 *
 * // Register later
 * plugin.registerHandler(handler)
 * ```
 *
 * @param T the event type
 * @param handler the event callback
 * @return a typed event handler instance
 */
inline fun <reified T : IBaseEvent<*>> eventHandler(
    noinline handler: (T) -> Unit
): TypedEventHandler<T> = TypedEventHandler(T::class.java, handler)

/**
 * A typed event handler that can be registered with a plugin.
 *
 * @property eventClass the class of events this handler processes
 * @property handler the callback function
 */
class TypedEventHandler<T : IBaseEvent<*>>(
    val eventClass: Class<T>,
    val handler: (T) -> Unit
)

/**
 * Registers a pre-created typed event handler.
 *
 * @param handler the handler to register
 */
@Suppress("UNCHECKED_CAST")
fun <T : IBaseEvent<*>> JavaPlugin.registerHandler(handler: TypedEventHandler<T>) {
    val eventClass = handler.eventClass as Class<IBaseEvent<Any>>
    val consumer = Consumer<IBaseEvent<Any>> { event -> handler.handler(event as T) }
    eventRegistry.registerGlobal(eventClass, consumer)
}
