package io.github.hytalekt.kytale.test

import com.hypixel.hytale.event.EventPriority
import com.hypixel.hytale.event.EventRegistration
import com.hypixel.hytale.event.EventRegistry
import com.hypixel.hytale.event.IBaseEvent
import com.hypixel.hytale.event.IEventRegistry
import com.hypixel.hytale.logger.HytaleLogger
import com.hypixel.hytale.protocol.MaybeBool
import com.hypixel.hytale.server.core.Message
import com.hypixel.hytale.server.core.plugin.JavaPlugin
import com.hypixel.hytale.server.core.plugin.PluginBase
import com.hypixel.hytale.server.core.task.TaskRegistry
import com.hypixel.hytale.server.core.universe.PlayerRef
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.ScheduledFuture
import java.util.function.Consumer
import java.util.logging.Level

// =============================================================================
// Message Utilities
// =============================================================================

/**
 * A test-friendly Message wrapper that captures all styling and content.
 * Since Message is a concrete class, we provide utilities to inspect it.
 */
object MessageCapture {
    /**
     * Gets the raw text from a message, or null if it's a translation message
     */
    fun getRawText(message: Message): String? = message.rawText

    /**
     * Gets the message ID (for i18n messages)
     */
    fun getMessageId(message: Message): String? = message.messageId

    /**
     * Checks if the message has bold styling
     */
    fun isBold(message: Message): Boolean =
        message.formattedMessage.bold == MaybeBool.True

    /**
     * Checks if the message has italic styling
     */
    fun isItalic(message: Message): Boolean =
        message.formattedMessage.italic == MaybeBool.True

    /**
     * Checks if the message has underlined styling
     */
    fun isUnderlined(message: Message): Boolean =
        message.formattedMessage.underlined == MaybeBool.True

    /**
     * Checks if the message has monospace styling
     */
    fun isMonospace(message: Message): Boolean =
        message.formattedMessage.monospace == MaybeBool.True

    /**
     * Gets the color hex string from the message
     */
    fun getColorHex(message: Message): String? = message.color

    /**
     * Gets the children messages
     */
    fun getChildren(message: Message): List<Message> =
        message.children?.toList() ?: emptyList()

    /**
     * Recursively extracts all raw text from a message and its children
     */
    fun getAllText(message: Message): String = buildString {
        message.rawText?.let { append(it) }
        message.children?.forEach { child ->
            append(getAllText(child))
        }
    }
}

// =============================================================================
// Message Capture for PlayerRef
// =============================================================================

/**
 * Captures messages sent to players for test assertions.
 * Thread-safe for concurrent test scenarios.
 */
class MessageRecorder {
    private val _messages = CopyOnWriteArrayList<CapturedMessage>()

    /** All captured messages */
    val messages: List<CapturedMessage> get() = _messages.toList()

    /** The last captured message, or null if none */
    val lastMessage: CapturedMessage? get() = _messages.lastOrNull()

    /** Number of captured messages */
    val size: Int get() = _messages.size

    /** Whether any messages have been captured */
    val isEmpty: Boolean get() = _messages.isEmpty()

    /** Whether any messages have been captured */
    val isNotEmpty: Boolean get() = _messages.isNotEmpty()

    /**
     * Records a message
     */
    fun record(playerUuid: UUID, message: Message) {
        _messages.add(CapturedMessage(playerUuid, message, System.currentTimeMillis()))
    }

    /**
     * Clears all captured messages
     */
    fun clear() {
        _messages.clear()
    }

    /**
     * Gets all messages sent to a specific player
     */
    fun messagesFor(playerUuid: UUID): List<CapturedMessage> =
        _messages.filter { it.playerUuid == playerUuid }

    /**
     * Gets all raw text from captured messages
     */
    fun allRawText(): List<String?> =
        _messages.map { it.message.rawText }

    /**
     * Checks if any message contains the given text
     */
    fun containsText(text: String): Boolean =
        _messages.any { MessageCapture.getAllText(it.message).contains(text) }

    /**
     * Finds messages containing the given text
     */
    fun findByText(text: String): List<CapturedMessage> =
        _messages.filter { MessageCapture.getAllText(it.message).contains(text) }
}

/**
 * A captured message with metadata
 */
data class CapturedMessage(
    val playerUuid: UUID,
    val message: Message,
    val timestamp: Long,
) {
    val rawText: String? get() = message.rawText
    val messageId: String? get() = message.messageId
    val allText: String get() = MessageCapture.getAllText(message)
}

// =============================================================================
// Fake PlayerRef
// =============================================================================

/**
 * Creates a fake PlayerRef for testing.
 *
 * Note: PlayerRef requires complex initialization with PacketHandler, ChunkTracker, etc.
 * This class wraps the essential data for test scenarios where you need to verify
 * player-related behavior without a real server connection.
 *
 * @param uuid The player's unique identifier
 * @param username The player's display name
 * @param worldUuid The UUID of the world the player is in (nullable)
 * @param language The player's language preference
 * @param messageRecorder Optional recorder to capture sent messages
 */
class FakePlayerRef(
    val uuid: UUID = UUID.randomUUID(),
    val username: String = "TestPlayer",
    var worldUuid: UUID? = null,
    var language: String = "en",
    private val messageRecorder: MessageRecorder? = null,
) {
    private val _sentMessages = CopyOnWriteArrayList<Message>()

    /** All messages sent to this player */
    val sentMessages: List<Message> get() = _sentMessages.toList()

    /** The last message sent to this player */
    val lastMessage: Message? get() = _sentMessages.lastOrNull()

    /**
     * Simulates sending a message to this player
     */
    fun sendMessage(message: Message) {
        _sentMessages.add(message)
        messageRecorder?.record(uuid, message)
    }

    /**
     * Clears the sent messages history
     */
    fun clearMessages() {
        _sentMessages.clear()
    }

    /**
     * Checks if the player received a message containing the given text
     */
    fun receivedMessageContaining(text: String): Boolean =
        _sentMessages.any { MessageCapture.getAllText(it).contains(text) }

    /**
     * Gets all raw text from sent messages
     */
    fun allReceivedText(): List<String?> =
        _sentMessages.map { it.rawText }
}

/**
 * Creates a FakePlayerRef with default values
 */
fun mockPlayerRef(
    uuid: UUID = UUID.randomUUID(),
    username: String = "TestPlayer",
    worldUuid: UUID? = null,
    language: String = "en",
    messageRecorder: MessageRecorder? = null,
): FakePlayerRef = FakePlayerRef(uuid, username, worldUuid, language, messageRecorder)

// =============================================================================
// Fake EventRegistry
// =============================================================================

/**
 * A captured event registration for test assertions
 */
data class CapturedEventRegistration<KeyType, EventType : IBaseEvent<KeyType>>(
    val eventClass: Class<*>,
    val key: KeyType?,
    val priority: Short,
    val consumer: Consumer<EventType>,
    val isGlobal: Boolean,
    val isUnhandled: Boolean,
)

/**
 * A fake EventRegistry that captures registrations for testing.
 *
 * This allows tests to verify that events are being registered correctly
 * without requiring a real event bus infrastructure.
 */
class FakeEventRegistry {
    private val _registrations = CopyOnWriteArrayList<CapturedEventRegistration<*, *>>()

    /** All captured event registrations */
    val registrations: List<CapturedEventRegistration<*, *>> get() = _registrations.toList()

    /** Number of registrations */
    val size: Int get() = _registrations.size

    /** Whether any events have been registered */
    val isEmpty: Boolean get() = _registrations.isEmpty()

    /**
     * Registers a global event listener
     */
    fun <KeyType, EventType : IBaseEvent<KeyType>> registerGlobal(
        eventClass: Class<in EventType>,
        consumer: Consumer<EventType>,
    ) {
        registerGlobal(EventPriority.NORMAL.value, eventClass, consumer)
    }

    /**
     * Registers a global event listener with priority
     */
    fun <KeyType, EventType : IBaseEvent<KeyType>> registerGlobal(
        priority: EventPriority,
        eventClass: Class<in EventType>,
        consumer: Consumer<EventType>,
    ) {
        registerGlobal(priority.value, eventClass, consumer)
    }

    /**
     * Registers a global event listener with numeric priority
     */
    fun <KeyType, EventType : IBaseEvent<KeyType>> registerGlobal(
        priority: Short,
        eventClass: Class<in EventType>,
        consumer: Consumer<EventType>,
    ) {
        _registrations.add(
            CapturedEventRegistration(
                eventClass = eventClass,
                key = null,
                priority = priority,
                consumer = consumer,
                isGlobal = true,
                isUnhandled = false,
            ),
        )
    }

    /**
     * Registers a keyed event listener
     */
    fun <KeyType, EventType : IBaseEvent<KeyType>> register(
        eventClass: Class<in EventType>,
        key: KeyType,
        consumer: Consumer<EventType>,
    ) {
        register(EventPriority.NORMAL.value, eventClass, key, consumer)
    }

    /**
     * Registers a keyed event listener with priority
     */
    fun <KeyType, EventType : IBaseEvent<KeyType>> register(
        priority: EventPriority,
        eventClass: Class<in EventType>,
        key: KeyType,
        consumer: Consumer<EventType>,
    ) {
        register(priority.value, eventClass, key, consumer)
    }

    /**
     * Registers a keyed event listener with numeric priority
     */
    fun <KeyType, EventType : IBaseEvent<KeyType>> register(
        priority: Short,
        eventClass: Class<in EventType>,
        key: KeyType,
        consumer: Consumer<EventType>,
    ) {
        _registrations.add(
            CapturedEventRegistration(
                eventClass = eventClass,
                key = key,
                priority = priority,
                consumer = consumer,
                isGlobal = false,
                isUnhandled = false,
            ),
        )
    }

    /**
     * Registers an unhandled event listener
     */
    fun <KeyType, EventType : IBaseEvent<KeyType>> registerUnhandled(
        eventClass: Class<in EventType>,
        consumer: Consumer<EventType>,
    ) {
        registerUnhandled(EventPriority.NORMAL.value, eventClass, consumer)
    }

    /**
     * Registers an unhandled event listener with priority
     */
    fun <KeyType, EventType : IBaseEvent<KeyType>> registerUnhandled(
        priority: EventPriority,
        eventClass: Class<in EventType>,
        consumer: Consumer<EventType>,
    ) {
        registerUnhandled(priority.value, eventClass, consumer)
    }

    /**
     * Registers an unhandled event listener with numeric priority
     */
    fun <KeyType, EventType : IBaseEvent<KeyType>> registerUnhandled(
        priority: Short,
        eventClass: Class<in EventType>,
        consumer: Consumer<EventType>,
    ) {
        _registrations.add(
            CapturedEventRegistration(
                eventClass = eventClass,
                key = null,
                priority = priority,
                consumer = consumer,
                isGlobal = false,
                isUnhandled = true,
            ),
        )
    }

    /**
     * Clears all registrations
     */
    fun clear() {
        _registrations.clear()
    }

    /**
     * Gets all global registrations
     */
    fun globalRegistrations(): List<CapturedEventRegistration<*, *>> =
        _registrations.filter { it.isGlobal }

    /**
     * Gets all keyed registrations
     */
    fun keyedRegistrations(): List<CapturedEventRegistration<*, *>> =
        _registrations.filter { !it.isGlobal && !it.isUnhandled }

    /**
     * Gets all unhandled registrations
     */
    fun unhandledRegistrations(): List<CapturedEventRegistration<*, *>> =
        _registrations.filter { it.isUnhandled }

    /**
     * Gets registrations for a specific event class
     */
    fun <T : IBaseEvent<*>> registrationsFor(eventClass: Class<T>): List<CapturedEventRegistration<*, *>> =
        _registrations.filter { it.eventClass == eventClass }

    /**
     * Checks if any listener is registered for the given event class
     */
    fun <T : IBaseEvent<*>> hasListenerFor(eventClass: Class<T>): Boolean =
        _registrations.any { it.eventClass == eventClass }

    /**
     * Dispatches an event to all matching listeners (for testing event handlers)
     */
    @Suppress("UNCHECKED_CAST")
    fun <KeyType, EventType : IBaseEvent<KeyType>> dispatch(event: EventType) {
        val eventClass = event::class.java

        // Dispatch to global listeners
        _registrations
            .filter { it.isGlobal && it.eventClass.isAssignableFrom(eventClass) }
            .sortedBy { it.priority }
            .forEach { registration ->
                (registration.consumer as Consumer<EventType>).accept(event)
            }

        // Dispatch to keyed listeners (would need event.getKey() in real impl)
        // For testing, we just dispatch to all matching keyed listeners
        _registrations
            .filter { !it.isGlobal && !it.isUnhandled && it.eventClass.isAssignableFrom(eventClass) }
            .sortedBy { it.priority }
            .forEach { registration ->
                (registration.consumer as Consumer<EventType>).accept(event)
            }
    }
}

// =============================================================================
// Fake TaskRegistry
// =============================================================================

/**
 * A captured task for test assertions
 */
data class CapturedTask(
    val task: Any,
    val timestamp: Long = System.currentTimeMillis(),
)

/**
 * A fake TaskRegistry that captures task registrations for testing.
 */
class FakeTaskRegistry {
    private val _tasks = CopyOnWriteArrayList<CapturedTask>()

    /** All registered tasks */
    val tasks: List<CapturedTask> get() = _tasks.toList()

    /** Number of registered tasks */
    val size: Int get() = _tasks.size

    /** Whether any tasks have been registered */
    val isEmpty: Boolean get() = _tasks.isEmpty()

    /**
     * Registers a task (CompletableFuture)
     */
    fun registerTask(task: CompletableFuture<Void>) {
        _tasks.add(CapturedTask(task))
    }

    /**
     * Clears all registered tasks
     */
    fun clear() {
        _tasks.clear()
    }
}

// =============================================================================
// Fake Logger
// =============================================================================

/**
 * Log level for captured log entries
 */
enum class LogLevel {
    DEBUG,
    INFO,
    WARN,
    ERROR,
}

/**
 * A captured log entry
 */
data class LogEntry(
    val level: LogLevel,
    val message: String,
    val throwable: Throwable? = null,
    val timestamp: Long = System.currentTimeMillis(),
)

/**
 * A fake logger that captures log messages for testing.
 */
class FakeLogger(val name: String = "TestLogger") {
    private val _entries = CopyOnWriteArrayList<LogEntry>()

    /** All captured log entries */
    val entries: List<LogEntry> get() = _entries.toList()

    /** Number of log entries */
    val size: Int get() = _entries.size

    /** Whether any entries have been logged */
    val isEmpty: Boolean get() = _entries.isEmpty()

    fun debug(message: String) {
        _entries.add(LogEntry(LogLevel.DEBUG, message))
    }

    fun info(message: String) {
        _entries.add(LogEntry(LogLevel.INFO, message))
    }

    fun warn(message: String) {
        _entries.add(LogEntry(LogLevel.WARN, message))
    }

    fun warn(message: String, throwable: Throwable) {
        _entries.add(LogEntry(LogLevel.WARN, message, throwable))
    }

    fun error(message: String) {
        _entries.add(LogEntry(LogLevel.ERROR, message))
    }

    fun error(message: String, throwable: Throwable) {
        _entries.add(LogEntry(LogLevel.ERROR, message, throwable))
    }

    /**
     * Clears all log entries
     */
    fun clear() {
        _entries.clear()
    }

    /**
     * Gets all entries at a specific level
     */
    fun entriesAt(level: LogLevel): List<LogEntry> =
        _entries.filter { it.level == level }

    /**
     * Gets all info-level entries
     */
    fun infoEntries(): List<LogEntry> = entriesAt(LogLevel.INFO)

    /**
     * Gets all warn-level entries
     */
    fun warnEntries(): List<LogEntry> = entriesAt(LogLevel.WARN)

    /**
     * Gets all error-level entries
     */
    fun errorEntries(): List<LogEntry> = entriesAt(LogLevel.ERROR)

    /**
     * Checks if any log message contains the given text
     */
    fun containsMessage(text: String): Boolean =
        _entries.any { it.message.contains(text) }

    /**
     * Finds entries containing the given text
     */
    fun findByMessage(text: String): List<LogEntry> =
        _entries.filter { it.message.contains(text) }

    /**
     * Checks if any error was logged
     */
    fun hasErrors(): Boolean = _entries.any { it.level == LogLevel.ERROR }

    /**
     * Checks if any warning was logged
     */
    fun hasWarnings(): Boolean = _entries.any { it.level == LogLevel.WARN }
}

// =============================================================================
// Fake Plugin Context
// =============================================================================

/**
 * A fake plugin context that combines all test utilities.
 *
 * This provides a convenient way to set up a complete test environment
 * with a logger, event registry, task registry, and message recorder.
 *
 * @param name The plugin name for logging
 */
class FakePluginContext(val name: String = "TestPlugin") {
    /** The fake logger */
    val logger = FakeLogger(name)

    /** The fake event registry */
    val eventRegistry = FakeEventRegistry()

    /** The fake task registry */
    val taskRegistry = FakeTaskRegistry()

    /** The message recorder for capturing player messages */
    val messageRecorder = MessageRecorder()

    /** List of fake players in this context */
    private val _players = mutableMapOf<UUID, FakePlayerRef>()

    /** All fake players */
    val players: Map<UUID, FakePlayerRef> get() = _players.toMap()

    /**
     * Creates a new fake player in this context
     */
    fun createPlayer(
        uuid: UUID = UUID.randomUUID(),
        username: String = "TestPlayer",
        worldUuid: UUID? = null,
        language: String = "en",
    ): FakePlayerRef {
        val player = FakePlayerRef(uuid, username, worldUuid, language, messageRecorder)
        _players[uuid] = player
        return player
    }

    /**
     * Gets a player by UUID
     */
    fun getPlayer(uuid: UUID): FakePlayerRef? = _players[uuid]

    /**
     * Removes a player from this context
     */
    fun removePlayer(uuid: UUID): FakePlayerRef? = _players.remove(uuid)

    /**
     * Clears all state (logs, events, tasks, messages, players)
     */
    fun reset() {
        logger.clear()
        eventRegistry.clear()
        taskRegistry.clear()
        messageRecorder.clear()
        _players.clear()
    }
}

/**
 * Creates a FakePluginContext with the given name
 */
fun mockPlugin(name: String = "TestPlugin"): FakePluginContext = FakePluginContext(name)

// =============================================================================
// Test DSL Helpers
// =============================================================================

/**
 * Creates a MessageRecorder for capturing messages in tests
 */
fun captureMessages(): MessageRecorder = MessageRecorder()

/**
 * Runs a block with a fresh FakePluginContext and returns it
 */
inline fun withMockPlugin(
    name: String = "TestPlugin",
    block: FakePluginContext.() -> Unit,
): FakePluginContext {
    val context = FakePluginContext(name)
    context.block()
    return context
}

/**
 * Extension to easily send a message from a FakePlayerRef to test
 */
fun FakePlayerRef.receive(message: Message) = sendMessage(message)

/**
 * Extension to easily send a text message
 */
fun FakePlayerRef.receiveText(text: String) = sendMessage(Message.raw(text))

// =============================================================================
// Kotest Matchers (optional utilities for more readable assertions)
// =============================================================================

/**
 * Asserts that the message recorder contains a message with the given text
 */
infix fun MessageRecorder.shouldContain(text: String) {
    if (!containsText(text)) {
        throw AssertionError("Expected messages to contain '$text' but found: ${allRawText()}")
    }
}

/**
 * Asserts that the message recorder does not contain a message with the given text
 */
infix fun MessageRecorder.shouldNotContain(text: String) {
    if (containsText(text)) {
        throw AssertionError("Expected messages to not contain '$text' but found: ${allRawText()}")
    }
}

/**
 * Asserts that the FakePlayerRef received a message containing the given text
 */
infix fun FakePlayerRef.shouldHaveReceived(text: String) {
    if (!receivedMessageContaining(text)) {
        throw AssertionError("Expected player to receive message containing '$text' but received: ${allReceivedText()}")
    }
}

/**
 * Asserts that the FakePlayerRef did not receive a message containing the given text
 */
infix fun FakePlayerRef.shouldNotHaveReceived(text: String) {
    if (receivedMessageContaining(text)) {
        throw AssertionError("Expected player to not receive message containing '$text' but received: ${allReceivedText()}")
    }
}

/**
 * Asserts that the FakeLogger logged a message containing the given text
 */
infix fun FakeLogger.shouldHaveLogged(text: String) {
    if (!containsMessage(text)) {
        throw AssertionError("Expected logger to contain '$text' but found: ${entries.map { it.message }}")
    }
}

/**
 * Asserts that the FakeLogger did not log a message containing the given text
 */
infix fun FakeLogger.shouldNotHaveLogged(text: String) {
    if (containsMessage(text)) {
        throw AssertionError("Expected logger to not contain '$text' but found: ${entries.map { it.message }}")
    }
}

/**
 * Asserts that the FakeEventRegistry has a listener for the given event class
 */
infix fun <T : IBaseEvent<*>> FakeEventRegistry.shouldHaveListenerFor(eventClass: Class<T>) {
    if (!hasListenerFor(eventClass)) {
        throw AssertionError("Expected event registry to have listener for ${eventClass.simpleName}")
    }
}
