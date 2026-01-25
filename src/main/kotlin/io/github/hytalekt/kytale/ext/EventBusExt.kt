package io.github.hytalekt.kytale.ext

import com.hypixel.hytale.event.EventBus
import com.hypixel.hytale.event.EventBusRegistry
import com.hypixel.hytale.event.IBaseEvent
import com.hypixel.hytale.event.IEvent

/**
 * Reified version of [EventBus.getRegistry]
 *
 * @see com.hypixel.hytale.event.EventBus.getRegistry
 */
inline fun <KeyType, reified EventType : IBaseEvent<KeyType>> EventBus.getRegistry(): EventBusRegistry<KeyType, EventType, *> =
    getRegistry(EventType::class.java)

/**
 * Reified version of [EventBus.getSyncRegistry]
 *
 * @see com.hypixel.hytale.event.EventBus.getSyncRegistry
 */
inline fun <KeyType, reified EventType : IEvent<KeyType>> EventBus.getSyncRegistry(): EventBusRegistry<KeyType, EventType, *> =
    getSyncRegistry(EventType::class.java)
