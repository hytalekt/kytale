package io.github.hytalekt.kytale.ext

import com.hypixel.hytale.event.EventPriority
import com.hypixel.hytale.event.EventRegistration
import com.hypixel.hytale.event.IAsyncEvent
import com.hypixel.hytale.event.IBaseEvent
import com.hypixel.hytale.event.IEventRegistry
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer
import java.util.function.Function

inline fun <reified EventType : IBaseEvent<Void>> IEventRegistry.register(
    consumer: Consumer<EventType>,
): EventRegistration<Void, EventType>? = register(EventType::class.java, consumer)

inline fun <reified EventType : IBaseEvent<Void>> IEventRegistry.register(
    priority: EventPriority,
    consumer: Consumer<EventType>,
): EventRegistration<Void, EventType>? = register(priority, EventType::class.java, consumer)

inline fun <reified EventType : IBaseEvent<Void>> IEventRegistry.register(
    priority: Short,
    consumer: Consumer<EventType>,
): EventRegistration<Void, EventType>? = register(priority, EventType::class.java, consumer)

inline fun <KeyType : Any, reified EventType : IBaseEvent<KeyType>> IEventRegistry.register(
    key: KeyType,
    consumer: Consumer<EventType>,
): EventRegistration<KeyType, EventType>? = register(EventType::class.java, key, consumer)

inline fun <KeyType : Any, reified EventType : IBaseEvent<KeyType>> IEventRegistry.register(
    priority: EventPriority,
    key: KeyType,
    consumer: Consumer<EventType>,
): EventRegistration<KeyType, EventType>? = register(priority, EventType::class.java, key, consumer)

inline fun <KeyType : Any, reified EventType : IBaseEvent<KeyType>> IEventRegistry.register(
    priority: Short,
    key: KeyType,
    consumer: Consumer<EventType>,
): EventRegistration<KeyType, EventType>? = register(priority, EventType::class.java, key, consumer)

inline fun <reified EventType : IAsyncEvent<Void>> IEventRegistry.registerAsync(
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<Void, EventType>? = registerAsync(EventType::class.java, handler)

inline fun <reified EventType : IAsyncEvent<Void>> IEventRegistry.registerAsync(
    priority: EventPriority,
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<Void, EventType>? = registerAsync(priority, EventType::class.java, handler)

inline fun <reified EventType : IAsyncEvent<Void>> IEventRegistry.registerAsync(
    priority: Short,
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<Void, EventType>? = registerAsync(priority, EventType::class.java, handler)

inline fun <KeyType : Any, reified EventType : IAsyncEvent<KeyType>> IEventRegistry.registerAsync(
    key: KeyType,
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<KeyType, EventType>? = registerAsync(EventType::class.java, key, handler)

inline fun <KeyType : Any, reified EventType : IAsyncEvent<KeyType>> IEventRegistry.registerAsync(
    priority: EventPriority,
    key: KeyType,
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<KeyType, EventType>? = registerAsync(priority, EventType::class.java, key, handler)

inline fun <KeyType : Any, reified EventType : IAsyncEvent<KeyType>> IEventRegistry.registerAsync(
    priority: Short,
    key: KeyType,
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<KeyType, EventType>? = registerAsync(priority, EventType::class.java, key, handler)

inline fun <KeyType, reified EventType : IBaseEvent<KeyType>> IEventRegistry.registerGlobal(
    consumer: Consumer<EventType>,
): EventRegistration<KeyType, EventType>? = registerGlobal(EventType::class.java, consumer)

inline fun <KeyType, reified EventType : IBaseEvent<KeyType>> IEventRegistry.registerGlobal(
    priority: EventPriority,
    consumer: Consumer<EventType>,
): EventRegistration<KeyType, EventType>? = registerGlobal(priority, EventType::class.java, consumer)

inline fun <KeyType, reified EventType : IBaseEvent<KeyType>> IEventRegistry.registerGlobal(
    priority: Short,
    consumer: Consumer<EventType>,
): EventRegistration<KeyType, EventType>? = registerGlobal(priority, EventType::class.java, consumer)

inline fun <KeyType, reified EventType : IAsyncEvent<KeyType>> IEventRegistry.registerAsyncGlobal(
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<KeyType, EventType>? = registerAsyncGlobal(EventType::class.java, handler)

inline fun <KeyType, reified EventType : IAsyncEvent<KeyType>> IEventRegistry.registerAsyncGlobal(
    priority: EventPriority,
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<KeyType, EventType>? = registerAsyncGlobal(priority, EventType::class.java, handler)

inline fun <KeyType, reified EventType : IAsyncEvent<KeyType>> IEventRegistry.registerAsyncGlobal(
    priority: Short,
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<KeyType, EventType>? = registerAsyncGlobal(priority, EventType::class.java, handler)

inline fun <KeyType, reified EventType : IBaseEvent<KeyType>> IEventRegistry.registerUnhandled(
    consumer: Consumer<EventType>,
): EventRegistration<KeyType, EventType>? = registerUnhandled(EventType::class.java, consumer)

inline fun <KeyType, reified EventType : IBaseEvent<KeyType>> IEventRegistry.registerUnhandled(
    priority: EventPriority,
    consumer: Consumer<EventType>,
): EventRegistration<KeyType, EventType>? = registerUnhandled(priority, EventType::class.java, consumer)

inline fun <KeyType, reified EventType : IBaseEvent<KeyType>> IEventRegistry.registerUnhandled(
    priority: Short,
    consumer: Consumer<EventType>,
): EventRegistration<KeyType, EventType>? = registerUnhandled(priority, EventType::class.java, consumer)

inline fun <KeyType, reified EventType : IAsyncEvent<KeyType>> IEventRegistry.registerAsyncUnhandled(
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<KeyType, EventType>? = registerAsyncUnhandled(EventType::class.java, handler)

inline fun <KeyType, reified EventType : IAsyncEvent<KeyType>> IEventRegistry.registerAsyncUnhandled(
    priority: EventPriority,
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<KeyType, EventType>? = registerAsyncUnhandled(priority, EventType::class.java, handler)

inline fun <KeyType, reified EventType : IAsyncEvent<KeyType>> IEventRegistry.registerAsyncUnhandled(
    priority: Short,
    handler: Function<CompletableFuture<EventType>, CompletableFuture<EventType>>,
): EventRegistration<KeyType, EventType>? = registerAsyncUnhandled(priority, EventType::class.java, handler)
