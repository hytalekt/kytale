package io.github.hytalekt.kytale.ext

import com.hypixel.hytale.event.EventBus
import com.hypixel.hytale.event.EventBusRegistry
import com.hypixel.hytale.event.IBaseEvent
import com.hypixel.hytale.event.IEvent

inline fun <KeyType, reified EventType : IBaseEvent<KeyType>> EventBus.getRegistry(): EventBusRegistry<KeyType, EventType, *> =
    getRegistry(EventType::class.java)

inline fun <KeyType, reified EventType : IEvent<KeyType>> EventBus.getSyncRegistry(): EventBusRegistry<KeyType, EventType, *> =
    getSyncRegistry(EventType::class.java)
