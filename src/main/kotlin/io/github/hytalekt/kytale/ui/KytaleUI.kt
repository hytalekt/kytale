package io.github.hytalekt.kytale.ui

import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage
import com.hypixel.hytale.server.core.ui.DropdownEntryInfo
import com.hypixel.hytale.server.core.ui.LocalizableString
import com.hypixel.hytale.server.core.ui.Value
import com.hypixel.hytale.server.core.ui.builder.EventData
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

@DslMarker
annotation class KytaleUIDsl

@KytaleUIDsl
class UIBuilder(
    private val commandBuilder: UICommandBuilder,
    private val eventBuilder: UIEventBuilder,
) {
    fun append(
        uiPath: String,
        elementName: String? = null,
    ) = apply {
        if (elementName != null) {
            commandBuilder.append(uiPath, elementName)
        } else {
            commandBuilder.append(uiPath)
        }
    }

    fun appendInline(
        uiPath: String,
        elementName: String,
    ) = apply {
        commandBuilder.appendInline(uiPath, elementName)
    }

    fun insertBefore(
        beforeElement: String,
        uiPath: String,
    ) = apply {
        commandBuilder.insertBefore(beforeElement, uiPath)
    }

    fun insertBeforeInline(
        beforeElement: String,
        uiPath: String,
    ) = apply {
        commandBuilder.insertBeforeInline(beforeElement, uiPath)
    }

    fun clear(selector: String) =
        apply {
            commandBuilder.clear(selector)
        }

    fun remove(selector: String) =
        apply {
            commandBuilder.remove(selector)
        }

    infix fun String.set(value: String) {
        commandBuilder.set(this, value)
    }

    infix fun String.set(value: Boolean) {
        commandBuilder.set(this, value)
    }

    infix fun String.set(value: Int) {
        commandBuilder.set(this, value)
    }

    infix fun String.set(value: Float) {
        commandBuilder.set(this, value)
    }

    infix fun String.set(value: Double) {
        commandBuilder.set(this, value)
    }

    infix fun String.set(value: Message) {
        commandBuilder.set(this, value)
    }

    infix fun <T> String.set(value: Value<T>) {
        commandBuilder.set(this, value)
    }

    infix fun <T> String.set(value: List<T>) {
        commandBuilder.set(this, value)
    }

    infix fun <T> String.set(value: Array<T>) {
        commandBuilder.set(this, value)
    }

    fun String.setNull() {
        commandBuilder.setNull(this)
    }

    fun String.setObject(value: Any) {
        commandBuilder.setObject(this, value)
    }

    fun dropdown(
        selector: String,
        block: DropdownBuilder.() -> Unit,
    ) {
        val builder = DropdownBuilder()
        builder.block()
        commandBuilder.set("$selector #Input.Entries", builder.entries)
        if (builder.defaultValue != null) {
            commandBuilder.set("$selector #Input.Value", builder.defaultValue!!)
        }
    }

    fun onActivate(
        selector: String,
        data: EventData? = null,
        consumeEvent: Boolean = false,
    ) = apply {
        if (data != null) {
            eventBuilder.addEventBinding(CustomUIEventBindingType.Activating, selector, data, consumeEvent)
        } else {
            eventBuilder.addEventBinding(CustomUIEventBindingType.Activating, selector, consumeEvent)
        }
    }

    fun onRightClick(
        selector: String,
        data: EventData? = null,
        consumeEvent: Boolean = false,
    ) = apply {
        if (data != null) {
            eventBuilder.addEventBinding(CustomUIEventBindingType.RightClicking, selector, data, consumeEvent)
        } else {
            eventBuilder.addEventBinding(CustomUIEventBindingType.RightClicking, selector, consumeEvent)
        }
    }

    fun onDoubleClick(
        selector: String,
        data: EventData? = null,
        consumeEvent: Boolean = false,
    ) = apply {
        if (data != null) {
            eventBuilder.addEventBinding(CustomUIEventBindingType.DoubleClicking, selector, data, consumeEvent)
        } else {
            eventBuilder.addEventBinding(CustomUIEventBindingType.DoubleClicking, selector, consumeEvent)
        }
    }

    fun onValueChange(
        selector: String,
        data: EventData? = null,
        consumeEvent: Boolean = false,
    ) = apply {
        if (data != null) {
            eventBuilder.addEventBinding(CustomUIEventBindingType.ValueChanged, selector, data, consumeEvent)
        } else {
            eventBuilder.addEventBinding(CustomUIEventBindingType.ValueChanged, selector, consumeEvent)
        }
    }

    fun onMouseEnter(
        selector: String,
        data: EventData? = null,
        consumeEvent: Boolean = false,
    ) = apply {
        if (data != null) {
            eventBuilder.addEventBinding(CustomUIEventBindingType.MouseEntered, selector, data, consumeEvent)
        } else {
            eventBuilder.addEventBinding(CustomUIEventBindingType.MouseEntered, selector, consumeEvent)
        }
    }

    fun onMouseExit(
        selector: String,
        data: EventData? = null,
        consumeEvent: Boolean = false,
    ) = apply {
        if (data != null) {
            eventBuilder.addEventBinding(CustomUIEventBindingType.MouseExited, selector, data, consumeEvent)
        } else {
            eventBuilder.addEventBinding(CustomUIEventBindingType.MouseExited, selector, consumeEvent)
        }
    }

    fun onDismiss(
        selector: String,
        data: EventData? = null,
        consumeEvent: Boolean = false,
    ) = apply {
        if (data != null) {
            eventBuilder.addEventBinding(CustomUIEventBindingType.Dismissing, selector, data, consumeEvent)
        } else {
            eventBuilder.addEventBinding(CustomUIEventBindingType.Dismissing, selector, consumeEvent)
        }
    }

    fun event(
        type: CustomUIEventBindingType,
        selector: String,
        data: EventData? = null,
        consumeEvent: Boolean = false,
    ) = apply {
        if (data != null) {
            eventBuilder.addEventBinding(type, selector, data, consumeEvent)
        } else {
            eventBuilder.addEventBinding(type, selector, consumeEvent)
        }
    }
}

@KytaleUIDsl
class DropdownBuilder {
    internal val entries = mutableListOf<DropdownEntryInfo>()
    internal var defaultValue: String? = null

    fun entry(
        label: String,
        value: String,
    ) {
        entries.add(DropdownEntryInfo(LocalizableString.fromString(label), value))
    }

    fun entry(
        label: LocalizableString,
        value: String,
    ) {
        entries.add(DropdownEntryInfo(label, value))
    }

    fun default(value: String) {
        defaultValue = value
    }
}

abstract class KytaleUI<T>(
    playerRef: PlayerRef,
    lifetime: CustomPageLifetime,
    eventDataCodec: BuilderCodec<T>,
) : InteractiveCustomUIPage<T>(playerRef, lifetime, eventDataCodec) {
    override fun build(
        ref: Ref<EntityStore?>,
        commandBuilder: UICommandBuilder,
        eventBuilder: UIEventBuilder,
        store: Store<EntityStore?>,
    ) {
        val builder = UIBuilder(commandBuilder, eventBuilder)
        builder.buildUI()
    }

    abstract fun UIBuilder.buildUI()
}
