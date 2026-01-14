package io.github.hytalekt.kytale.interaction

import com.hypixel.hytale.server.core.entity.InteractionChain
import com.hypixel.hytale.server.core.entity.InteractionContext

/**
 * DSL for creating interaction chains in a fluent way.
 *
 * Interactions in Hytale are chains of actions that can fork, have cooldowns,
 * and synchronize state. This DSL makes building complex interactions cleaner.
 *
 * Example usage:
 * ```
 * val miningInteraction = interaction {
 *     cooldown(1000) // 1 second cooldown
 *
 *     action {
 *         // Start mining animation
 *         playAnimation("mining")
 *     }
 *
 *     delay(500)
 *
 *     action {
 *         // Deal damage to block
 *         damageBlock(targetBlock, 1)
 *     }
 *
 *     fork {
 *         // Branch 1: If block breaks
 *         condition { blockHealth <= 0 }
 *         action {
 *             breakBlock(targetBlock)
 *             dropItems()
 *         }
 *     }
 *
 *     fork {
 *         // Branch 2: Continue mining
 *         condition { blockHealth > 0 }
 *         repeatChain()
 *     }
 * }
 * ```
 */
@DslMarker
annotation class InteractionDsl

/**
 * Main builder for interaction chains
 */
@InteractionDsl
class InteractionChainBuilder {
    private val actions = mutableListOf<InteractionAction>()
    private var cooldownMs: Long = 0
    private val forks = mutableListOf<ForkBuilder>()

    /**
     * Sets the cooldown for this interaction
     */
    fun cooldown(milliseconds: Long) {
        cooldownMs = milliseconds
    }

    /**
     * Adds an action to the interaction chain
     */
    fun action(block: InteractionContext.() -> Unit) {
        actions.add(InteractionAction.Simple(block))
    }

    /**
     * Adds a delay before the next action
     */
    fun delay(milliseconds: Long) {
        actions.add(InteractionAction.Delay(milliseconds))
    }

    /**
     * Creates a fork in the interaction chain
     */
    fun fork(configure: ForkBuilder.() -> Unit) {
        val fork = ForkBuilder()
        fork.configure()
        forks.add(fork)
    }

    /**
     * Repeats the entire chain
     */
    fun repeatChain() {
        actions.add(InteractionAction.Repeat)
    }

    /**
     * Cancels the interaction chain
     */
    fun cancel() {
        actions.add(InteractionAction.Cancel)
    }

    /**
     * Builds the interaction chain
     */
    fun build(): InteractionChain {
        TODO("Implement InteractionChain building")
    }
}

/**
 * Builder for fork conditions
 */
@InteractionDsl
class ForkBuilder {
    private var condition: (InteractionContext.() -> Boolean)? = null
    private val actions = mutableListOf<InteractionAction>()

    /**
     * Sets the condition for this fork to execute
     */
    fun condition(predicate: InteractionContext.() -> Boolean) {
        condition = predicate
    }

    /**
     * Adds an action to this fork
     */
    fun action(block: InteractionContext.() -> Unit) {
        actions.add(InteractionAction.Simple(block))
    }

    /**
     * Adds a delay in this fork
     */
    fun delay(milliseconds: Long) {
        actions.add(InteractionAction.Delay(milliseconds))
    }

    /**
     * Repeats from this fork
     */
    fun repeatChain() {
        actions.add(InteractionAction.Repeat)
    }
}

/**
 * Represents different types of interaction actions
 */
sealed class InteractionAction {
    data class Simple(
        val block: InteractionContext.() -> Unit,
    ) : InteractionAction()

    data class Delay(
        val milliseconds: Long,
    ) : InteractionAction()

    object Repeat : InteractionAction()

    object Cancel : InteractionAction()
}

/**
 * DSL function to create an interaction chain
 */
fun interaction(configure: InteractionChainBuilder.() -> Unit): InteractionChain = InteractionChainBuilder().apply(configure).build()

/**
 * Extension for InteractionContext to access stored variables
 */
operator fun InteractionContext.get(key: String): Any? {
    TODO("Get variable from context")
}

/**
 * Extension for InteractionContext to set stored variables
 */
operator fun InteractionContext.set(
    key: String,
    value: Any,
) {
    TODO("Set variable in context")
}
