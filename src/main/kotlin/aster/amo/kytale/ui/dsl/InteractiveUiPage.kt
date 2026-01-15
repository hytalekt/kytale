package aster.amo.kytale.ui.dsl

import com.hypixel.hytale.codec.Codec
import com.hypixel.hytale.codec.KeyedCodec
import com.hypixel.hytale.codec.builder.BuilderCodec
import com.hypixel.hytale.codec.codecs.simple.IntegerCodec
import com.hypixel.hytale.codec.codecs.simple.FloatCodec
import com.hypixel.hytale.codec.codecs.simple.BooleanCodec
import com.hypixel.hytale.component.Ref
import com.hypixel.hytale.component.Store
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType
import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage
import com.hypixel.hytale.server.core.ui.builder.EventData
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder
import com.hypixel.hytale.server.core.universe.PlayerRef
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore

/**
 * Value types that Hytale UI elements can send.
 */
enum class ValueType {
    STRING,
    INT,
    FLOAT,
    BOOLEAN
}

/**
 * Dynamic event data container using a Map with Any values.
 * Each page generates its own codec with fields for its specific elements.
 * Values are stored as their native types (Int, Float, Boolean, String) and
 * converted to strings when accessed through ValueEventContext.
 */
class DynamicEventData {
    val data = mutableMapOf<String, Any?>()

    operator fun get(key: String): Any? = data[key]
    operator fun set(key: String, value: Any?) {
        data[key] = value
    }

    fun getString(key: String): String? = data[key]?.toString()
}

/**
 * Event context for value change handlers that includes the new value.
 */
data class ValueEventContext(
    val player: Player,
    val playerRef: PlayerRef,
    val value: String?
) {
    val intValue: Int? get() = value?.toIntOrNull()
    val floatValue: Float? get() = value?.toFloatOrNull()
    val boolValue: Boolean? get() = value?.toBooleanStrictOrNull()
    val stringValue: String? get() = value
}

/**
 * Type alias for value change handlers that receive the actual value.
 */
typealias OnValueChange = ValueEventContext.() -> Unit

/**
 * Registered event handler with its type information.
 */
internal sealed class RegisteredHandler {
    abstract val elementId: String
    abstract val eventType: CustomUIEventBindingType

    data class Activate(
        override val elementId: String,
        val handler: OnActivate
    ) : RegisteredHandler() {
        override val eventType = CustomUIEventBindingType.Activating
    }

    /** Value change handler - fires when value changes, includes actual value from Hytale */
    data class ValueChange(
        override val elementId: String,
        val handler: OnValueChange,
        val valueType: ValueType
    ) : RegisteredHandler() {
        override val eventType = CustomUIEventBindingType.ValueChanged
    }
}

/**
 * Builder for creating interactive UI pages with integrated DSL and event handling.
 *
 * Uses dynamic codec generation following the Hytale pattern where:
 * - Button/activate events use key "Button" with value = element ID
 * - Value change events use key "@{elementId}" with value = evaluated selector expression
 *
 * Usage:
 * ```kotlin
 * val myPage = interactivePage("MyPage") {
 *     width = 400
 *     height = 300
 *
 *     title {
 *         label {
 *             text = "My Title"
 *             style = UiLabelStyle(fontSize = 24, textColor = "#ffffff")
 *         }
 *     }
 *
 *     content {
 *         textButton("ClickMe") {
 *             text = "Click Me"
 *             onClick = { player.sendMessage(Message.raw("Clicked!")) }
 *         }
 *
 *         slider("Volume") {
 *             min = 0
 *             max = 100
 *             value = 50
 *             onChange = { player.sendMessage(Message.raw("Volume: $intValue")) }
 *         }
 *     }
 * }
 *
 * // Open the page
 * player.pageManager.openCustomPage(ref, store, myPage.createPage(playerRef))
 * ```
 */
@UiDsl
class InteractiveUiPage(
    val name: String,
    val lifetime: CustomPageLifetime = CustomPageLifetime.CanDismiss
) {
    /**
     * Width of the page container.
     */
    var width: Int = 550

    /**
     * Height of the page container.
     */
    var height: Int = 650

    /**
     * Path to the .ui file to load. This is required.
     * Use generateUiFile() to generate the content for this file.
     */
    var uiFilePath: String = "Pages/Kytale/$name.ui"

    private var titleBuilder: (InteractiveGroupBuilder.() -> Unit)? = null
    private var contentBuilder: (InteractiveGroupBuilder.() -> Unit)? = null
    private var backButtonEnabled: Boolean = true

    private var titleGroupBuilder: InteractiveGroupBuilder? = null
    private var contentGroupBuilder: InteractiveGroupBuilder? = null
    private val handlers = mutableListOf<RegisteredHandler>()

    companion object {
        /** Key used for button/activate events - value is the button ID */
        const val KEY_BUTTON = "Button"
    }

    /**
     * Define the title section of the page.
     */
    fun title(block: InteractiveGroupBuilder.() -> Unit) {
        titleBuilder = block
    }

    /**
     * Define the content section of the page.
     */
    fun content(block: InteractiveGroupBuilder.() -> Unit) {
        contentBuilder = block
    }

    /**
     * Enable or disable the back button.
     */
    fun backButton(enabled: Boolean) {
        backButtonEnabled = enabled
    }

    /**
     * Build the internal structure and collect handlers.
     */
    private fun buildStructure() {
        handlers.clear()

        // Build title group
        titleGroupBuilder = titleBuilder?.let { builder ->
            InteractiveGroupBuilder().apply { builder() }
        }

        // Build content group
        contentGroupBuilder = contentBuilder?.let { builder ->
            InteractiveGroupBuilder().apply { builder() }
        }

        // Collect handlers from interactive elements
        titleGroupBuilder?.let { collectHandlers(it) }
        contentGroupBuilder?.let { collectHandlers(it) }
    }

    private fun collectHandlers(groupBuilder: InteractiveGroupBuilder) {
        for (wrapper in groupBuilder.wrappers) {
            val id = wrapper.elementId

            when (wrapper) {
                is InteractiveTextButton -> {
                    wrapper.onClick?.let { handlers.add(RegisteredHandler.Activate(id, it)) }
                }
                is InteractiveButton -> {
                    wrapper.onClick?.let { handlers.add(RegisteredHandler.Activate(id, it)) }
                }
                is InteractiveSlider -> {
                    wrapper.onChange?.let { handlers.add(RegisteredHandler.ValueChange(id, it, ValueType.INT)) }
                }
                is InteractiveFloatSlider -> {
                    wrapper.onChange?.let { handlers.add(RegisteredHandler.ValueChange(id, it, ValueType.FLOAT)) }
                }
                is InteractiveCheckBox -> {
                    wrapper.onChange?.let { handlers.add(RegisteredHandler.ValueChange(id, it, ValueType.BOOLEAN)) }
                }
                is InteractiveTextField -> {
                    wrapper.onChange?.let { handlers.add(RegisteredHandler.ValueChange(id, it, ValueType.STRING)) }
                    wrapper.onSubmit?.let { handlers.add(RegisteredHandler.Activate(id, it)) }
                }
                is InteractiveNumberField -> {
                    wrapper.onChange?.let { handlers.add(RegisteredHandler.ValueChange(id, it, ValueType.FLOAT)) }
                }
                is InteractiveDropdownBox -> {
                    wrapper.onChange?.let { handlers.add(RegisteredHandler.ValueChange(id, it, ValueType.INT)) }
                }
                is InteractiveColorPicker -> {
                    wrapper.onChange?.let { handlers.add(RegisteredHandler.ValueChange(id, it, ValueType.STRING)) }
                }
                is InteractiveColorPickerDropdownBox -> {
                    wrapper.onChange?.let { handlers.add(RegisteredHandler.ValueChange(id, it, ValueType.STRING)) }
                }
                is InteractiveItemSlotButton -> {
                    wrapper.onClick?.let { handlers.add(RegisteredHandler.Activate(id, it)) }
                }
            }
        }
    }

    /**
     * Build a dynamic codec for this page based on its handlers.
     *
     * Following the Hytale pattern:
     * - "Button" field captures which button was activated (value = element ID)
     * - "@{elementId}" fields capture value changes (value = actual element value)
     *
     * Uses appropriate codec types based on element type:
     * - Sliders/DropdownBox: INT
     * - FloatSliders/NumberFields: FLOAT
     * - CheckBoxes: BOOLEAN
     * - TextFields/ColorPickers: STRING
     */
    @Suppress("UNCHECKED_CAST")
    private fun buildCodec(pageHandlers: List<RegisteredHandler>): BuilderCodec<DynamicEventData> {
        val builder = BuilderCodec.builder(
            DynamicEventData::class.java,
            ::DynamicEventData
        )

        // Add "Button" field for all activate events
        // When a button is clicked, this field receives the button's element ID
        builder.addField(
            KeyedCodec(KEY_BUTTON, Codec.STRING),
            { data, v -> data[KEY_BUTTON] = v },
            { it[KEY_BUTTON] as? String }
        )

        // Add a field for each value change handler using @ElementId pattern
        // When a value changes, the corresponding field receives the actual value
        val valueHandlers = pageHandlers.filterIsInstance<RegisteredHandler.ValueChange>()
        for (handler in valueHandlers) {
            val key = "@${handler.elementId}"

            when (handler.valueType) {
                ValueType.STRING -> {
                    builder.addField(
                        KeyedCodec(key, Codec.STRING),
                        { data, v -> data[key] = v },
                        { it[key] as? String }
                    )
                }
                ValueType.INT -> {
                    builder.addField(
                        KeyedCodec(key, IntegerCodec()),
                        { data, v -> data[key] = v },
                        { it[key] as? Int }
                    )
                }
                ValueType.FLOAT -> {
                    builder.addField(
                        KeyedCodec(key, FloatCodec()),
                        { data, v -> data[key] = v },
                        { it[key] as? Float }
                    )
                }
                ValueType.BOOLEAN -> {
                    builder.addField(
                        KeyedCodec(key, BooleanCodec()),
                        { data, v -> data[key] = v },
                        { it[key] as? Boolean }
                    )
                }
            }
        }

        return builder.build()
    }

    /**
     * Generate the .ui file content from the DSL structure.
     */
    fun generateUiFile(): String {
        buildStructure()
        return buildString {
            appendLine("\$C = \"../../Common.ui\";")
            appendLine()
            appendLine("\$C.@PageOverlay {")
            appendLine("  \$C.@Container {")
            appendLine("    Anchor: (Width: $width, Height: $height);")
            appendLine()

            // Title section
            titleGroupBuilder?.let { builder ->
                if (builder.group.children.isNotEmpty()) {
                    appendLine("    #Title {")
                    appendLine("      Group {")
                    for (child in builder.group.children) {
                        append(child.serialize(4))
                    }
                    appendLine("      }")
                    appendLine("    }")
                    appendLine()
                }
            }

            // Content section
            appendLine("    #Content {")
            appendLine("      LayoutMode: Top;")
            contentGroupBuilder?.let { builder ->
                for (child in builder.group.children) {
                    append(child.serialize(3))
                }
            }
            appendLine("    }")
            appendLine("  }")
            appendLine("}")

            // Back button
            if (backButtonEnabled) {
                appendLine()
                appendLine("\$C.@BackButton {}")
            }
        }
    }

    /**
     * Create an InteractiveCustomUIPage instance for this page definition.
     */
    fun createPage(playerRef: PlayerRef): InteractiveCustomUIPage<DynamicEventData> {
        buildStructure()
        val pageHandlers = handlers.toList()
        val pageUiPath = uiFilePath
        val codec = buildCodec(pageHandlers)

        return object : InteractiveCustomUIPage<DynamicEventData>(
            playerRef,
            lifetime,
            codec
        ) {
            override fun build(
                ref: Ref<EntityStore>,
                uiCommandBuilder: UICommandBuilder,
                uiEventBuilder: UIEventBuilder,
                store: Store<EntityStore>
            ) {
                // Load UI file
                uiCommandBuilder.append(pageUiPath)

                // Register event bindings for all handlers
                for (handler in pageHandlers) {
                    val selector = "#${handler.elementId}"

                    val eventData = when (handler) {
                        is RegisteredHandler.Activate -> {
                            // Activate events: key="Button", value=elementId (literal)
                            EventData.of(KEY_BUTTON, handler.elementId)
                        }
                        is RegisteredHandler.ValueChange -> {
                            // Value change events: key="@ElementId", value=selector expression
                            // Hytale evaluates "#ElementId.Value" and puts the result in the field
                            EventData.of("@${handler.elementId}", "$selector.Value")
                        }
                    }

                    uiEventBuilder.addEventBinding(
                        handler.eventType,
                        selector,
                        eventData,
                        false
                    )
                }
            }

            override fun handleDataEvent(
                ref: Ref<EntityStore>,
                store: Store<EntityStore>,
                data: DynamicEventData
            ) {
                val player = store.getComponent(ref, Player.getComponentType()) ?: return

                // Check for button activation
                val buttonId = data[KEY_BUTTON] as? String
                if (buttonId != null) {
                    val handler = pageHandlers.find {
                        it is RegisteredHandler.Activate && it.elementId == buttonId
                    } as? RegisteredHandler.Activate

                    if (handler != null) {
                        val context = EventContext(player, playerRef)
                        handler.handler.invoke(context)
                    }
                    return
                }

                // Check for value changes
                for (handler in pageHandlers) {
                    if (handler is RegisteredHandler.ValueChange) {
                        val key = "@${handler.elementId}"
                        val rawValue = data[key]
                        if (rawValue != null) {
                            // Convert to string for ValueEventContext
                            val stringValue = rawValue.toString()
                            val context = ValueEventContext(player, playerRef, stringValue)
                            handler.handler.invoke(context)
                            return
                        }
                    }
                }
            }
        }
    }
}

/**
 * Factory function to create an interactive UI page.
 *
 * @param name The name of the page (used for identification/debugging)
 * @param lifetime The page lifetime behavior (default: CanDismiss)
 * @param block The DSL block to define the page content
 * @return An InteractiveUiPage builder
 */
fun interactivePage(
    name: String,
    lifetime: CustomPageLifetime = CustomPageLifetime.CanDismiss,
    block: InteractiveUiPage.() -> Unit
): InteractiveUiPage {
    return InteractiveUiPage(name, lifetime).apply(block)
}

/**
 * Extension function to open an interactive page for a player.
 */
fun Player.openInteractivePage(playerRef: PlayerRef, page: InteractiveUiPage) {
    val ref = playerRef.reference ?: return
    this.pageManager.openCustomPage(ref, ref.store, page.createPage(playerRef))
}
