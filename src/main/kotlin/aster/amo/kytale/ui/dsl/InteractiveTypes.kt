package aster.amo.kytale.ui.dsl

import com.hypixel.hytale.server.core.entity.entities.Player
import com.hypixel.hytale.server.core.universe.PlayerRef

/**
 * Event handler context provided to all event callbacks.
 */
data class EventContext(
    val player: Player,
    val playerRef: PlayerRef
)

/**
 * Type alias for activation event handlers (button clicks, etc.).
 */
typealias OnActivate = EventContext.() -> Unit

/**
 * Base class for interactive element wrappers that combine a UI element with event handlers.
 */
sealed class InteractiveWrapper {
    abstract val elementId: String
    abstract val element: UiElement
}
