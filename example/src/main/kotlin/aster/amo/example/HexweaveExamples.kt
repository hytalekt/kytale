package aster.amo.example

import aster.amo.hexweave.dsl.HexweaveBuilder
import aster.amo.hexweave.dsl.events.HexweaveEventsScope
import aster.amo.hexweave.dsl.players.HexweavePlayersScope
import aster.amo.hexweave.dsl.systems.HexweaveSystemsScope
import aster.amo.hexweave.dsl.tasks.HexweaveTasksScope
import aster.amo.kytale.extension.info
import com.hypixel.hytale.event.EventPriority
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent
import com.hypixel.hytale.server.core.event.events.player.PlayerDisconnectEvent
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause
import com.hypixel.hytale.server.core.modules.entity.damage.DamageSystems
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Comprehensive examples showcasing all Hexweave DSL features.
 *
 * This file demonstrates:
 * - Player lifecycle hooks (join, leave, chat)
 * - Command registration with subcommands
 * - Scheduled/repeating tasks
 * - ECS event systems (damage, tick, generic)
 * - EventBus event listeners with priorities
 *
 * ## Quick Reference
 *
 * | Feature | DSL Block | Purpose |
 * |---------|-----------|---------|
 * | Players | `players { }` | Join/leave/chat hooks |
 * | Commands | `commands { }` | Player commands |
 * | Tasks | `tasks { }` | Scheduled coroutine tasks |
 * | Systems | `systems { }` | ECS event systems |
 * | Events | `events { }` | EventBus listeners |
 */
object HexweaveExamples {

    // =========================================================================
    // PLAYER LIFECYCLE EXAMPLES
    // =========================================================================

    /**
     * Demonstrates player lifecycle hooks.
     *
     * Available hooks:
     * - `onJoin { }` - Player connects to the server
     * - `onLeave { }` - Player disconnects from the server
     * - `onChat { }` - Player sends a chat message
     *
     * Context provides:
     * - `playerRef` - The PlayerRef component
     * - `logger` - Plugin logger
     */
    fun HexweavePlayersScope.playerExamples() {
        // Basic join handler
        onJoin {
            logger.info { "Player joined: ${playerRef.uuid}" }
            playerRef.sendMessage(Message.raw("Welcome to the server!"))
        }

        // Leave handler with cleanup
        onLeave {
            logger.info { "Player left: ${playerRef.uuid}" }
            // Clean up any player-specific data
            playerSessionData.remove(playerRef.uuid)
        }

        // Chat handler with command detection
        onChat {
            val message = chatEvent.content.trim()

            // Simple chat commands
            when {
                message.equals("!help", ignoreCase = true) -> {
                    chatEvent.sender.sendMessage(Message.raw("Available commands: !help, !ping, !time"))
                }
                message.equals("!ping", ignoreCase = true) -> {
                    chatEvent.sender.sendMessage(Message.raw("Pong!"))
                }
                message.startsWith("!") -> {
                    chatEvent.sender.sendMessage(Message.raw("Unknown command: $message"))
                }
            }
        }
    }

    // =========================================================================
    // COMMAND EXAMPLES
    // =========================================================================

    /**
     * Demonstrates command registration.
     *
     * Features:
     * - `literal(name, description) { }` - Register a command
     * - `subcommand(name, description) { }` - Add subcommands
     * - `aliases(...)` - Add command aliases
     * - `executes { }` - Async handler (any sender)
     * - `executesPlayer { }` - Sync handler (player only)
     *
     * Player context provides:
     * - `player` - The Player entity
     * - `world` - The player's world (nullable)
     * - `sendMessage(msg)` - Send a message
     * - `onWorld { playerRef -> }` - Execute on world thread
     */
    fun aster.amo.hexweave.dsl.commands.HexweaveCommandsScope.commandExamples() {
        // Simple command
        literal("hello", "Say hello") {
            executesPlayer {
                sendMessage(Message.raw("Hello!"))
            }
        }

        // Command with subcommands
        literal("server", "Server management commands") {
            aliases("srv")

            // Status subcommand
            subcommand("status", "Show server status") {
                executesPlayer {
                    sendMessage(Message.raw("=== Server Status ==="))
                    sendMessage(Message.raw("Players online: ${playerSessionData.size}"))
                    sendMessage(Message.raw("Server uptime: Running"))
                }
            }

            // Info subcommand with world access
            subcommand("info", "Show your info") {
                executesPlayer {
                    sendMessage(Message.raw("=== Your Info ==="))

                    // Safe world thread access
                    onWorld { playerRef ->
                        sendMessage(Message.raw("UUID: ${playerRef.uuid}"))
                        sendMessage(Message.raw("World: ${playerRef.worldUuid}"))
                    }
                }
            }

            // Nested subcommands
            subcommand("admin", "Admin commands") {
                subcommand("reload", "Reload configuration") {
                    executesPlayer {
                        sendMessage(Message.raw("Configuration reloaded!"))
                    }
                }

                subcommand("broadcast", "Broadcast a message") {
                    executesPlayer {
                        sendMessage(Message.raw("Usage: /server admin broadcast <message>"))
                    }
                }
            }
        }

        // Command with async execution
        literal("async", "Async command example") {
            executes { ctx ->
                ctx.sendMessage(Message.raw("Starting async work..."))
                // Can use coroutines here
                kotlinx.coroutines.delay(100)
                ctx.sendMessage(Message.raw("Async work completed!"))
            }
        }
    }

    // =========================================================================
    // TASK EXAMPLES
    // =========================================================================

    /**
     * Demonstrates scheduled tasks.
     *
     * Features:
     * - `repeating(name, delay, every) { }` - Repeating coroutine task
     *
     * Context provides:
     * - `logger` - Plugin logger
     * - Full coroutine support (delay, async, etc.)
     */
    fun HexweaveTasksScope.taskExamples() {
        // Heartbeat task - runs every 30 seconds
        repeating("heartbeat", every = 30.seconds) {
            logger.info { "[Heartbeat] Server is healthy" }
        }

        // Delayed repeating task - starts after 1 minute
        repeating("cleanup", delay = 1.minutes, every = 5.minutes) {
            logger.info { "[Cleanup] Running periodic cleanup..." }
            // Clean up expired data
            cleanupExpiredSessions()
        }

        // Fast tick task
        repeating("fast-tick", every = 1.seconds) {
            // Runs every second for time-sensitive operations
        }
    }

    // =========================================================================
    // ECS SYSTEM EXAMPLES
    // =========================================================================

    /**
     * Demonstrates ECS event systems.
     *
     * ## System Types
     *
     * | Type | Use Case |
     * |------|----------|
     * | `damageSystem` | Damage events with rich context |
     * | `tickSystem` | Per-tick entity processing |
     * | `entityEventSystem` | Any entity-level ECS event |
     * | `worldEventSystem` | Any world-level ECS event |
     *
     * ## Common Configuration
     *
     * - `filter { }` - Filter which events to process
     * - `dependencies { before<T>(); after<T>() }` - Execution ordering
     * - `priority` - Numeric priority (lower = earlier)
     * - `query { }` - Entity query for filtering
     */
    fun HexweaveSystemsScope.systemExamples() {
        // -----------------------------------------------------------------
        // DAMAGE SYSTEM EXAMPLES
        // -----------------------------------------------------------------

        // Prevent fall damage
        damageSystem("no-fall-damage") {
            filter { it.cause == DamageCause.FALL }
            dependencies {
                before<DamageSystems.ApplyDamage>()
            }
            onDamage {
                cancelDamage()
                playerRef?.sendMessage(Message.raw("Fall damage prevented!"))
            }
        }

        // Damage multiplier
        damageSystem("damage-multiplier") {
            filter { it.cause == DamageCause.PHYSICAL }
            onDamage {
                // Double physical damage
                damage.amount = damage.amount * 2
                logger.info { "Damage multiplied to ${damage.amount}" }
            }
        }

        // Damage logging
        damageSystem("damage-logger") {
            dependencies {
                after<DamageSystems.ApplyDamage>()
            }
            onDamage {
                if (!damage.isCancelled) {
                    logger.info { "Entity took ${damage.amount} ${damage.cause} damage" }
                }
            }
        }

        // -----------------------------------------------------------------
        // TICK SYSTEM EXAMPLES
        // -----------------------------------------------------------------
        //
        // Tick systems require ECS queries. Example usage:
        //
        // tickSystem("velocity-monitor") {
        //     query {
        //         ArchetypeQuery.builder<EntityStore>()
        //             .require(Velocity.getComponentType())
        //             .build()
        //     }
        //     every = 20 // Every 20 ticks (1 second at 20 TPS)
        //     onTick {
        //         // Access tick context:
        //         // - deltaTime: Time since last tick
        //         // - tickIndex: Current tick number
        //         // - index, chunk, store, commandBuffer: ECS access
        //     }
        // }
        //
        // tickSystem("fast-processor") {
        //     query { /* your entity query */ }
        //     every = 1  // Every tick
        //     onTick { /* High-frequency processing */ }
        // }

        // -----------------------------------------------------------------
        // GENERIC ENTITY EVENT SYSTEM
        // -----------------------------------------------------------------
        //
        // Use entityEventSystem for ANY EcsEvent type.
        // This is the most flexible approach - works with any event
        // without requiring specific Hexweave support.

        // Example: Block place handler (commented - requires actual event class)
        // entityEventSystem<EntityStore, PlaceBlockEvent>("block-place-handler") {
        //     priority = 10  // Optional priority
        //     filter { !it.isCancelled }
        //     dependencies {
        //         before<SomeOtherSystem>()
        //     }
        //     onEvent {
        //         // EntityEventContext provides:
        //         // - index: Entity index in chunk
        //         // - chunk: The archetype chunk
        //         // - store: The entity store
        //         // - commandBuffer: For modifications
        //         // - event: The event being processed
        //         // - logger: Plugin logger
        //
        //         val blockType = event.blockType
        //         logger.info { "Block placed: $blockType" }
        //     }
        // }

        // -----------------------------------------------------------------
        // GENERIC WORLD EVENT SYSTEM
        // -----------------------------------------------------------------
        //
        // Use worldEventSystem for world-level events (not per-entity).

        // Example: Moon phase handler (commented - requires actual event class)
        // worldEventSystem<EntityStore, MoonPhaseChangeEvent>("moon-handler") {
        //     onEvent {
        //         // WorldEventContext provides:
        //         // - store: The entity store
        //         // - commandBuffer: For modifications
        //         // - event: The event being processed
        //         // - logger: Plugin logger
        //
        //         when (event.newMoonPhase) {
        //             MoonPhase.FULL -> logger.info { "Full moon! Werewolves awaken..." }
        //             MoonPhase.NEW -> logger.info { "New moon - darkness falls" }
        //             else -> {}
        //         }
        //     }
        // }
    }

    // =========================================================================
    // EVENTBUS LISTENER EXAMPLES
    // =========================================================================

    /**
     * Demonstrates EventBus event listeners.
     *
     * ## Registration Modes
     *
     * | Method | Use Case |
     * |--------|----------|
     * | `listen<T> { }` | Global - receives ALL events |
     * | `listen<T>(priority) { }` | Global with priority |
     * | `listen<T>(filter) { }` | Global with filter |
     * | `listenKeyed<T, K>(key) { }` | Only events for specific key |
     * | `listenUnhandled<T> { }` | Fallback for unhandled events |
     * | `listenAsync<T> { }` | Async event handling |
     *
     * ## Priority Order
     *
     * `FIRST` → `EARLY` → `NORMAL` → `LATE` → `LAST`
     */
    fun HexweaveEventsScope.eventExamples() {
        // -----------------------------------------------------------------
        // BASIC GLOBAL LISTENERS
        // -----------------------------------------------------------------

        // Simple listener - receives all PlayerConnectEvent
        listen<PlayerConnectEvent> {
            logger.info { "Player connected: ${event.playerRef.uuid}" }
        }

        // Listener with priority - runs FIRST for validation
        listen<PlayerChatEvent>(EventPriority.FIRST) {
            // Check for banned words (runs before other handlers)
            if (event.content.contains("banned", ignoreCase = true)) {
                event.isCancelled = true
                event.sender.sendMessage(Message.raw("Message blocked."))
            }
        }

        // Listener with priority - runs LAST for logging
        listen<PlayerChatEvent>(EventPriority.LAST) {
            if (!event.isCancelled) {
                logger.info { "[Chat] ${event.sender.uuid}: ${event.content}" }
            }
        }

        // -----------------------------------------------------------------
        // FILTERED LISTENERS
        // -----------------------------------------------------------------

        // Filter predicate - only handle specific messages
        listen<PlayerChatEvent>({ it.content.startsWith("/") }) {
            logger.info { "Command detected: ${event.content}" }
        }

        // Filter with priority
        listen<PlayerChatEvent>(EventPriority.EARLY, { it.content.length > 100 }) {
            // Truncate long messages
            logger.info { "Long message from ${event.sender.uuid}" }
        }

        // -----------------------------------------------------------------
        // KEYED LISTENERS
        // -----------------------------------------------------------------
        //
        // Keyed listeners only receive events for a specific key.
        // Useful for player-specific or entity-specific handling.

        // Example: Track specific player (commented - needs actual UUID)
        // val targetPlayerId: UUID = UUID.fromString("...")
        // listenKeyed<PlayerChatEvent, UUID>(targetPlayerId) {
        //     // Only triggered for this specific player
        //     logger.info { "Target player said: ${event.content}" }
        // }

        // -----------------------------------------------------------------
        // UNHANDLED LISTENERS (FALLBACK)
        // -----------------------------------------------------------------
        //
        // Called when an event has a key but no keyed listener matched.

        // Catch-all for unhandled disconnect events
        listen<PlayerDisconnectEvent> {
            logger.info { "Player disconnected: ${event.playerRef.uuid}" }
        }

        // -----------------------------------------------------------------
        // ASYNC LISTENERS
        // -----------------------------------------------------------------
        //
        // For events that implement IAsyncEvent.
        // Enables non-blocking operations like database queries.

        // Example: Async player load (commented - requires actual async event)
        // listenAsync<AsyncPlayerLoadEvent> { future ->
        //     future.thenApply { event ->
        //         // Load player data asynchronously
        //         val data = loadPlayerFromDatabase(event.playerId)
        //         event.playerData = data
        //         event
        //     }
        // }

        // Async with priority
        // listenAsync<AsyncPlayerLoadEvent>(EventPriority.EARLY) { future ->
        //     future.thenApply { event ->
        //         // Validate before other handlers
        //         if (!isValidPlayer(event.playerId)) {
        //             event.isCancelled = true
        //         }
        //         event
        //     }
        // }
    }

    // =========================================================================
    // HELPER DATA & FUNCTIONS
    // =========================================================================

    /** Simple in-memory session storage for examples. */
    private val playerSessionData = ConcurrentHashMap<UUID, Long>()

    /** Clean up expired sessions. */
    private fun cleanupExpiredSessions() {
        val now = System.currentTimeMillis()
        val expireTime = 30 * 60 * 1000L // 30 minutes
        playerSessionData.entries.removeIf { (_, joinTime) ->
            now - joinTime > expireTime
        }
    }
}

/**
 * Extension function to apply all Hexweave examples.
 *
 * Usage:
 * ```kotlin
 * enableHexweave {
 *     applyAllExamples()
 * }
 * ```
 */
fun HexweaveBuilder.applyAllExamples() {
    players { HexweaveExamples.run { playerExamples() } }
    commands { HexweaveExamples.run { commandExamples() } }
    tasks { HexweaveExamples.run { taskExamples() } }
    systems { HexweaveExamples.run { systemExamples() } }
    events { HexweaveExamples.run { eventExamples() } }
}
