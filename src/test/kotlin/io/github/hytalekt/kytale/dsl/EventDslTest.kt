package io.github.hytalekt.kytale.dsl

import com.hypixel.hytale.event.IBaseEvent
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.nulls.shouldNotBeNull
import java.util.function.Consumer

/**
 * Tests for Event DSL.
 */
class EventDslTest : FunSpec({

    context("TypedEventHandler") {
        test("creates handler with correct event class") {
            val handler = eventHandler<TestEvent> { }

            handler.eventClass shouldBe TestEvent::class.java
        }

        test("handler function is stored") {
            val handler = eventHandler<TestEvent> { }

            handler.handler.shouldNotBeNull()
        }

        test("handler function is callable") {
            var receivedValue: String? = null
            val handler = eventHandler<TestEvent> { event ->
                receivedValue = event.data
            }

            val testEvent = TestEvent("test-data")
            handler.handler(testEvent)

            receivedValue shouldBe "test-data"
        }
    }

    context("eventHandler factory function") {
        test("preserves event type information") {
            val handler = eventHandler<TestEvent> { }

            (handler.eventClass == TestEvent::class.java) shouldBe true
        }

        test("handler receives correctly typed event") {
            var eventData: String? = null
            val handler = eventHandler<TestEvent> { event ->
                eventData = event.data
            }

            val event = TestEvent("hello")
            handler.handler(event)

            eventData shouldBe "hello"
        }
    }

    context("EventRegistration with interface") {
        test("can be instantiated with mock EventRegistrar") {
            val mockRegistrar = TestEventRegistrar()
            val registration = EventRegistration(mockRegistrar)

            registration.registrar shouldBe mockRegistrar
        }

        test("registers events through the registrar") {
            val mockRegistrar = TestEventRegistrar()
            val registration = EventRegistration(mockRegistrar)

            registration.on<TestEvent> { }

            mockRegistrar.registeredEvents.size shouldBe 1
            mockRegistrar.registeredEvents[0].first shouldBe TestEvent::class.java
        }

        test("registered handler is invoked correctly") {
            val mockRegistrar = TestEventRegistrar()
            val registration = EventRegistration(mockRegistrar)
            var receivedData: String? = null

            registration.on<TestEvent> { event ->
                receivedData = event.data
            }

            // Invoke the registered handler
            @Suppress("UNCHECKED_CAST")
            val handler = mockRegistrar.registeredEvents[0].second as Consumer<TestEvent>
            handler.accept(TestEvent("test-value"))

            receivedData shouldBe "test-value"
        }

        test("filtered handler only invokes when filter passes") {
            val mockRegistrar = TestEventRegistrar()
            val registration = EventRegistration(mockRegistrar)
            var invokeCount = 0

            registration.on<TestEvent>(
                filter = { it.data.startsWith("valid") }
            ) { invokeCount++ }

            @Suppress("UNCHECKED_CAST")
            val handler = mockRegistrar.registeredEvents[0].second as Consumer<TestEvent>

            handler.accept(TestEvent("valid-data"))
            handler.accept(TestEvent("invalid-data"))
            handler.accept(TestEvent("valid-again"))

            invokeCount shouldBe 2
        }
    }
})

/**
 * Test event implementation for unit testing.
 */
private class TestEvent(val data: String) : IBaseEvent<Unit>

/**
 * Test implementation of EventRegistrar for unit testing.
 */
private class TestEventRegistrar : EventRegistrar {
    val registeredEvents = mutableListOf<Pair<Class<*>, Consumer<*>>>()

    override fun <T : IBaseEvent<*>> registerEvent(eventClass: Class<T>, handler: Consumer<T>) {
        registeredEvents.add(eventClass to handler)
    }
}
