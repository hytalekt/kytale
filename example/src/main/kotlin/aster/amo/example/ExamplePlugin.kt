package aster.amo.example



import aster.amo.hexweave.enableHexweave

import aster.amo.kytale.KotlinPlugin

import aster.amo.kytale.coroutines.onAsync

import aster.amo.kytale.coroutines.onIO

import aster.amo.kytale.coroutines.asFuture

import aster.amo.kytale.dsl.command

import aster.amo.kytale.dsl.events

import aster.amo.kytale.dsl.jsonConfig

import aster.amo.kytale.dsl.schedule

import aster.amo.kytale.dsl.scheduleRepeating

import aster.amo.kytale.extension.*

import aster.amo.kytale.util.*

import com.hypixel.hytale.server.core.Message

import com.hypixel.hytale.server.core.event.events.player.PlayerChatEvent

import com.hypixel.hytale.server.core.event.events.player.PlayerConnectEvent

import com.hypixel.hytale.server.core.plugin.JavaPluginInit

import kotlinx.coroutines.delay

import kotlinx.coroutines.launch

import kotlinx.serialization.Serializable

import java.util.UUID

import kotlin.time.Duration.Companion.minutes

import kotlin.time.Duration.Companion.seconds



/**

 * Example plugin demonstrating HyKot features.

 *

 * This plugin showcases:

 * - KotlinPlugin base class with CoroutineScope

 * - Event DSL with reified type registration

 * - Command DSL with subcommands and coroutine execution

 * - JSON configuration with kotlinx.serialization

 * - Scheduled tasks with Kotlin Duration

 * - Caching utilities (ExpiringCache, LoadingCache)

 * - Cooldown management

 * - Validation utilities

 * - Math utilities

 * - Collection extensions

 * - Component/text formatting

 * - Async coroutine extensions

 */

class ExamplePlugin(init: JavaPluginInit) : KotlinPlugin(init) {



    /**

     * Plugin configuration using kotlinx.serialization.

     */

    private val config by jsonConfig<ExampleConfig>("config") { ExampleConfig() }



    /**

     * Tracks server uptime in seconds.

     */

    private var uptimeSeconds: Long = 0



    /**

     * Player data cache with 5-minute expiration.

     */

    private val playerDataCache = expiringCache<UUID, PlayerData>(5.minutes)



    /**

     * Command cooldowns per player.

     */

    private val teleportCooldown = playerCooldown(30.seconds)



    /**

     * Multi-action cooldown manager.

     */

    private val actionCooldowns = MultiCooldown<UUID>()



    /**

     * Simple player session cache.

     */

    private val sessionCache = simpleCache<UUID, Long>()



    override fun setup() {

        super.setup()

        logger.info { "ExamplePlugin setting up..." }



        // Register event handlers

        registerEvents()



        // Register commands

        registerCommands()



        // Showcase Hexweave helper layer
        // See HexweaveExamples.kt for comprehensive documentation of all features
        enableHexweave {
            // Player lifecycle hooks
            players {
                onJoin {
                    logger.info { "[Hexweave] Player joined: ${playerRef.uuid}" }
                    playerRef.sendMessage(Message.raw("[Hexweave] Welcome to the server!"))

                    // Track session start
                    sessionCache.put(playerRef.uuid, System.currentTimeMillis())
                }

                onLeave {
                    logger.info { "[Hexweave] Player left: ${playerRef.uuid}" }

                    // Clean up player data
                    sessionCache.remove(playerRef.uuid)
                    playerDataCache.remove(playerRef.uuid)
                }

                onChat {
                    val message = chatEvent.content.trim()
                    when {
                        message.equals("!hello", ignoreCase = true) -> {
                            chatEvent.sender.sendMessage(Message.raw("Hello from Hexweave!"))
                        }
                        message.equals("!uptime", ignoreCase = true) -> {
                            val uptime = formatDuration(uptimeSeconds)
                            chatEvent.sender.sendMessage(Message.raw("Server uptime: $uptime"))
                        }
                        message.equals("!players", ignoreCase = true) -> {
                            chatEvent.sender.sendMessage(Message.raw("Sessions: ${sessionCache.size}"))
                        }
                    }
                }
            }

            // Command registration with Hexweave context
            commands {
                literal("hexweave", "Hexweave helper commands") {
                    aliases("hw")

                    subcommand("ping", "Check if Hexweave is running") {
                        executesPlayer {
                            sendMessage(Message.raw("Hexweave is active!"))
                        }
                    }

                    subcommand("info", "Show player info") {
                        executesPlayer {
                            sendMessage(Message.raw("=== Player Info ==="))

                            // Safe world thread access
                            onWorld { playerRef ->
                                sendMessage(Message.raw("UUID: ${playerRef.uuid}"))
                                sendMessage(Message.raw("World: ${playerRef.worldUuid}"))
                            }
                        }
                    }

                    subcommand("status", "Show server status") {
                        executesPlayer {
                            sendMessage(Message.raw("=== Server Status ==="))
                            sendMessage(Message.raw("Uptime: ${formatDuration(uptimeSeconds)}"))
                            sendMessage(Message.raw("Sessions: ${sessionCache.size}"))
                            sendMessage(Message.raw("Cached players: ${playerDataCache.size}"))
                        }
                    }

                    subcommand("help", "Show available commands") {
                        executesPlayer {
                            sendMessage(Message.raw("=== Hexweave Commands ==="))
                            sendMessage(Message.raw("/hw ping - Check if Hexweave is running"))
                            sendMessage(Message.raw("/hw info - Show your player info"))
                            sendMessage(Message.raw("/hw status - Show server status"))
                            sendMessage(Message.raw("/hw help - This help message"))
                        }
                    }
                }
            }

            // Scheduled tasks with coroutine support
            tasks {
                repeating("hex-heartbeat", every = 30.seconds) {
                    logger.info { "[Hexweave] heartbeat - sessions: ${sessionCache.size}" }
                }
            }

            // ECS event systems - uncomment to enable
            // See HexweaveExamples.kt for more system examples
            //
            // systems {
            //     // Damage system example - prevent fall damage
            //     damageSystem("fall-handler") {
            //         filter { it.cause == DamageCause.FALL }
            //         dependencies {
            //             before<DamageSystems.ApplyDamage>()
            //         }
            //         onDamage {
            //             cancelDamage()
            //             playerRef?.sendMessage(Message.raw("Fall damage prevented!"))
            //         }
            //     }
            //
            //     // Tick system example - entity processing
            //     tickSystem("velocity-check") {
            //         query {
            //             ArchetypeQuery.builder<EntityStore>()
            //                 .require(Velocity.getComponentType())
            //                 .build()
            //         }
            //         every = 20 // Every 20 ticks
            //         onTick {
            //             // Process entities with velocity
            //         }
            //     }
            // }

            // EventBus listeners - uncomment to enable
            // See HexweaveExamples.kt for more event examples
            //
            // events {
            //     // Global listener with priority
            //     listen<PlayerChatEvent>(EventPriority.FIRST) {
            //         // Validate chat messages before other handlers
            //         if (event.content.contains("spam")) {
            //             event.isCancelled = true
            //         }
            //     }
            //
            //     // Filtered listener
            //     listen<PlayerChatEvent>({ it.content.startsWith("!") }) {
            //         logger.info { "Chat command: ${event.content}" }
            //     }
            // }
        }





        logger.info { "ExamplePlugin setup complete." }

    }



    override fun start() {

        super.start()

        logger.info { "ExamplePlugin starting..." }

        logger.info { "Welcome message: ${config.welcomeMessage}" }

        logger.info { "Max players: ${config.maxPlayers}" }



        // Launch coroutine directly - KotlinPlugin implements CoroutineScope

        launch {

            delay(100)

            logger.debug { "Async initialization complete" }

        }



        // Demonstrate utility extensions

        demonstrateUtilities()



        // Start scheduled tasks

        startScheduledTasks()



        logger.info { "ExamplePlugin started successfully!" }

    }



    override fun shutdown() {

        logger.info { "ExamplePlugin shutting down..." }

        // Caches and coroutines are automatically cleaned up by KotlinPlugin

        super.shutdown()

        logger.info { "ExamplePlugin shut down." }

    }



    /**

     * Registers event handlers using the Event DSL.

     */

    private fun registerEvents() {

        events {

            // Player connect event

            on { event: PlayerConnectEvent ->

                val playerId = event.playerRef.uuid

                sessionCache.put(playerId, System.currentTimeMillis())



                // Track join in cache

                playerDataCache.put(playerId, PlayerData(

                    joinTime = System.currentTimeMillis(),

                    messageCount = 0

                ))



                logger.info { "Player connected: $playerId" }

            }

        }

    }



    /**

     * Registers commands using the Command DSL.

     */

    private fun registerCommands() {

        // Main example command with subcommands

        command("example", "HyKot example commands") {

            aliases("ex", "hykot")



            // Status subcommand

            subcommand("status", "Show server status") {

                executes { ctx ->

                    val uptime = formatDuration(uptimeSeconds)

                    val cacheSize = playerDataCache.size

                    ctx.sendMessage(Message.raw("Uptime: $uptime, Cached players: $cacheSize"))

                }

            }



            // Async subcommand demonstrating coroutine execution

            subcommand("async", "Run async operation") {

                executes { ctx ->

                    ctx.sendMessage(Message.raw("Starting async operation..."))



                    // Demonstrate async context switching

                    val result = onAsync {

                        delay(100)

                        "Async work completed"

                    }



                    ctx.sendMessage(Message.raw(result))

                }

            }



            // Cache subcommand

            subcommand("cache", "Cache operations") {

                subcommand("clear", "Clear all caches") {

                    executes { ctx ->

                        playerDataCache.clear()

                        sessionCache.clear()

                        ctx.sendMessage(Message.raw("Caches cleared"))

                    }

                }



                subcommand("cleanup", "Remove expired entries") {

                    executes { ctx ->

                        playerDataCache.cleanup()

                        ctx.sendMessage(Message.raw("Cache cleanup completed"))

                    }

                }

            }



            // Default execution

            executes { ctx ->

                ctx.sendMessage(Message.raw("Usage: /example <status|async|cache>"))

            }

        }



        // Teleport command with cooldown

        command("exampleteleport", "Teleport with cooldown") {

            aliases("extp")



            executes { ctx ->

                // This would normally get the player's UUID

                val playerId = UUID.randomUUID() // Placeholder



                teleportCooldown.ifReady(playerId,

                    action = {

                        ctx.sendMessage(Message.raw("Teleporting..."))

                    },

                    fallback = { remaining ->

                        val seconds = remaining.inWholeSeconds

                        ctx.sendMessage(Message.raw("Cooldown: ${seconds}s remaining"))

                    }

                )

            }

        }

    }



    /**

     * Demonstrates HyKot utility extensions.

     */

    private fun demonstrateUtilities() {

        // String utilities

        val longMessage = "This is a very long message that should be truncated"

        logger.debug { "Truncated: ${longMessage.truncate(20)}" }

        logger.debug { "Centered: ${"Hello".center(20, '-')}" }



        // Number formatting

        val bigNumber = 1_500_000L

        logger.debug { "Formatted: ${bigNumber.formatWithCommas()}" }

        logger.debug { "Compact: ${bigNumber.formatCompact()}" }



        // Ordinals and pluralization

        logger.debug { "Ordinal: ${1.toOrdinal()}, ${2.toOrdinal()}, ${3.toOrdinal()}" }

        logger.debug { "Plural: ${1.pluralize("player")}, ${5.pluralize("player")}" }



        // Safe execution

        val result = safely { "Success value" }

        logger.debug { "Safe result: $result" }



        val withDefault = safelyOrDefault("default") {

            throw RuntimeException("This fails")

        }

        logger.debug { "With default: $withDefault" }



        // Result type for explicit error handling

        val parseResult = Result.catching { config.maxPlayers * 2 }

        parseResult

            .onSuccess { logger.debug { "Calculated: $it" } }

            .onFailure { error, _ -> logger.warn { "Failed: $error" } }



        // Validation

        val validation = validation<String>()

            .require(config.maxPlayers > 0) { "Max players must be positive" }

            .require(config.welcomeMessage.isNotBlank()) { "Welcome message required" }

            .build { "Config is valid" }



        validation.onSuccess { logger.debug { it } }



        // Math utilities

        val clamped = 150.0.clamp(0.0, 100.0)

        logger.debug { "Clamped: $clamped" }



        val lerped = lerp(0.0, 100.0, 0.5)

        logger.debug { "Lerped: $lerped" }



        val distance = distance3D(0.0, 0.0, 0.0, 10.0, 10.0, 10.0)

        logger.debug { "Distance: ${distance.format(2)}" }



        // Collection extensions

        val items = listOf("apple", "banana", "cherry", "date")

        val random = items.randomOrNull()

        logger.debug { "Random item: $random" }



        val counts = listOf("a", "b", "a", "c", "a", "b").countBy { it }

        logger.debug { "Counts: $counts" }



        val duplicates = listOf(1, 2, 2, 3, 3, 3).duplicates()

        logger.debug { "Duplicates: $duplicates" }



        // Message building with Hytale's native Message class

        val formattedMessage = message {

            colored(Colors.GREEN) { +"Server " }

            +"Status: "

            colored(Colors.GOLD) { +"Online" }

        }

        logger.debug { "Formatted message: ${formattedMessage.rawText}" }



        // Or use extension functions for simpler cases

        val simpleColored = coloredMessage(Colors.AQUA, "Hello World!")

        logger.debug { "Simple colored: ${simpleColored.rawText}" }



        // UUID parsing

        val validUuid = "550e8400-e29b-41d4-a716-446655440000"

        val parsed = validUuid.toUUIDOrNull()

        logger.debug { "Parsed UUID: $parsed" }

        logger.debug { "Is valid: ${validUuid.isValidUUID()}" }



        // Duration formatting

        logger.debug { "1 hour: ${formatDuration(3600)}" }

        logger.debug { "1 day: ${formatDuration(86400)}" }

        logger.debug { "Compact 1 day: ${formatDurationCompact(86400)}" }



        // Location utilities

        val spawn = location("world", 0.0, 64.0, 0.0)

        val target = location("world", 100.0, 64.0, 100.0)

        logger.debug { "Distance to target: ${spawn.distanceTo(target).format(2)}" }



        // Percentage formatting

        val ratio = 0.756

        logger.debug { "Percentage: ${ratio.toPercentString()}" }

    }



    /**

     * Demonstrates scheduled tasks using Kotlin Duration.

     */

    private fun startScheduledTasks() {

        // One-time delayed task

        schedule(delay = 5.seconds) {

            logger.info { "Delayed startup task executed" }

        }



        // Repeating task for uptime tracking

        scheduleRepeating(period = 1.seconds) {

            uptimeSeconds++

        }



        // Periodic cache cleanup (every 5 minutes)

        scheduleRepeating(delay = 1.minutes, period = 5.minutes) {

            playerDataCache.cleanup()

            actionCooldowns.cleanup()

            logger.debug { "Periodic cache cleanup completed" }

        }



        // Status logging (after 1 minute, every 5 minutes)

        scheduleRepeating(delay = 1.minutes, period = 5.minutes) {

            logger.info { "Status: uptime=${formatDuration(uptimeSeconds)}, cached=${playerDataCache.size}" }

        }



        // Async scheduled task

        schedule(delay = 10.seconds) {

            val data = onIO {

                // Simulate I/O operation

                delay(50)

                "Loaded data from I/O"

            }

            logger.debug { "I/O result: $data" }

        }

    }



    /**

     * Demonstrates asFuture for Java interop.

     */

    fun loadDataAsync() = asFuture {

        delay(100)

        PlayerData(System.currentTimeMillis(), 0)

    }

}



/**

 * Configuration data class for the example plugin.

 */

@Serializable

data class ExampleConfig(

    val welcomeMessage: String = "Welcome to the server!",

    val welcomeEnabled: Boolean = true,

    val maxPlayers: Int = 100,

    val serverName: String = "HyKot Example Server",

    val debugMode: Boolean = false

)



/**

 * Player data for caching demonstration.

 */

data class PlayerData(

    val joinTime: Long,

    var messageCount: Int

)

