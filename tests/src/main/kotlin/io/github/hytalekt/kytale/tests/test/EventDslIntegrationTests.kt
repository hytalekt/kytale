package io.github.hytalekt.kytale.tests.test

import io.github.hytalekt.kytale.dsl.eventHandler
import io.github.hytalekt.kytale.dsl.TypedEventHandler
import com.hypixel.hytale.event.IBaseEvent

/**
 * Integration tests for Event DSL.
 *
 * Tests that event handlers are correctly created and typed.
 * Event registration tests require actual event dispatch which happens at runtime.
 */
fun IntegrationTestRunner.eventDslTests() = suite("Event DSL") {

    test("TypedEventHandler stores correct event class") {
        val handler = eventHandler<TestIntegrationEvent> { }

        assertEquals(TestIntegrationEvent::class.java, handler.eventClass)
    }

    test("TypedEventHandler stores handler function") {
        val handler = eventHandler<TestIntegrationEvent> { }

        assertNotNull(handler.handler)
    }

    test("TypedEventHandler invokes handler with event") {
        var receivedValue: String? = null
        val handler = eventHandler<TestIntegrationEvent> { event ->
            receivedValue = event.data
        }

        val testEvent = TestIntegrationEvent("test-data")
        handler.handler(testEvent)

        assertEquals("test-data", receivedValue)
    }

    test("eventHandler preserves type information") {
        val handler = eventHandler<TestIntegrationEvent> { }

        assertTrue(handler.eventClass == TestIntegrationEvent::class.java)
    }

    test("handler receives correctly typed event") {
        var eventData: String? = null
        val handler = eventHandler<TestIntegrationEvent> { event ->
            eventData = event.data
        }

        val event = TestIntegrationEvent("hello")
        handler.handler(event)

        assertEquals("hello", eventData)
    }

    test("multiple handlers can be created for same event type") {
        var count = 0

        val handler1 = eventHandler<TestIntegrationEvent> { count++ }
        val handler2 = eventHandler<TestIntegrationEvent> { count++ }

        val event = TestIntegrationEvent("test")
        handler1.handler(event)
        handler2.handler(event)

        assertEquals(2, count)
    }

    test("handlers are independent") {
        var result1: String? = null
        var result2: String? = null

        val handler1 = eventHandler<TestIntegrationEvent> { result1 = "first-${it.data}" }
        val handler2 = eventHandler<TestIntegrationEvent> { result2 = "second-${it.data}" }

        val event = TestIntegrationEvent("value")
        handler1.handler(event)
        handler2.handler(event)

        assertEquals("first-value", result1)
        assertEquals("second-value", result2)
    }
}

/**
 * Test event implementation for integration testing.
 */
class TestIntegrationEvent(val data: String) : IBaseEvent<Unit>
