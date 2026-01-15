package aster.amo.hexweave

import aster.amo.hexweave.dsl.HexweaveBuilder
import aster.amo.hexweave.internal.HexweaveLifecycle
import com.hypixel.hytale.server.core.plugin.JavaPlugin

/**
 * Entry point for enabling the Hexweave helper layer within a plugin.
 *
 * Hexweave provides a DSL for common plugin patterns:
 * - Player lifecycle hooks (join/leave)
 * - Event subscriptions
 * - Command registration
 * - Task scheduling
 * - ECS event systems (damage, tick, generic)
 *
 * Example:
 * ```kotlin
 * class MyPlugin(init: JavaPluginInit) : KotlinPlugin(init) {
 *     override fun setup() {
 *         enableHexweave {
 *             players {
 *                 onJoin { logger.info { "Welcome ${playerRef.uuid}" } }
 *             }
 *             commands {
 *                 literal("hello", "Greet the player") {
 *                     executesPlayer { sendMessage(Message.raw("Hello!")) }
 *                 }
 *             }
 *             systems {
 *                 damageSystem("my-handler") {
 *                     filter { it.cause == DamageCause.FALL }
 *                     onDamage { cancelDamage() }
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
 */
fun JavaPlugin.enableHexweave(
    block: HexweaveBuilder.() -> Unit = {}
) {
    val scope = HexweaveLifecycle.ensureScope(this)
    HexweaveBuilder(this, scope).apply(block)
    HexweaveLifecycle.bootSystems(this)
}

/**
 * Optionally tear down Hexweave resources when a plugin is being disabled.
 */
fun JavaPlugin.disableHexweave() {
    HexweaveLifecycle.shutdown(this)
}
