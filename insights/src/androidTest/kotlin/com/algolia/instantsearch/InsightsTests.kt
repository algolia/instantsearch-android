package com.algolia.instantsearch

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.algolia.instantsearch.insights.*
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@RunWith(AndroidJUnit4::class)
class InsightsTests {

    private val context get() = InstrumentationRegistry.getContext()

    private val appId = "SPH6CBEPLC"
    private val apiKey = "064f4f03e7c37d8d7cfb40cdbf852f3d"
    private val indexName = "support_rmogos"
    private val eventParametersA get() = eventParameters("EventA")
    private val eventParametersB get() = eventParameters("EventB")
    private val configuration = Insights.Configuration(
        uploadIntervalInSeconds = 30L,
        connectTimeoutInMilliseconds = 5000,
        readTimeoutInMilliseconds = 5000
    )
    private val networkManager get() = NetworkManager(appId, apiKey, NetworkManager.Environment.Prod, configuration)

    private fun eventParameters(name: String): Map<String, Any> {
        return mapOf(
            "eventName" to name,
            "queryID" to "6de2f7eaa537fa93d8f8f05b927953b1",
            "position" to 1,
            "objectID" to "54675051",
            "indexName" to indexName,
            "timestamp" to System.currentTimeMillis()
        )
    }

    @Test
    fun testInitShouldFail() {
        try {
            Insights.shared("index")
        } catch (exception: Exception) {
            assertTrue(exception is InsightsException.CredentialsNotFound)
        }
    }

    @Test
    fun testInitShouldWork() {
        val insights = Insights.register(context, "testApp", "testKey", "index", configuration)
        val insightsShared = Insights.shared("index")

        assertEquals(insights, insightsShared)
    }

    @Test
    fun testConverterEvent() {
        val expected = Event.Click(mapOf("key1" to "value1"))
        val string = ConverterEventToString.convert(expected)
        val event = ConverterStringToEvent.convert(string)

        assertEquals(expected, event)
    }

    @Test
    fun testEventConsumer() {
        val preferences = context.sharedPreferences(indexName)
        val events = listOf(eventParametersA, eventParametersB).map { Event.Click(it) }
        val serializedEvents = ConverterEventToString.convert(events).toSet()

        preferences.events = serializedEvents

        assertEquals(serializedEvents, preferences.events)

        val failedEvents = preferences.consumeEvents {
            it.mapIndexed { index, serializedEvent ->
                NetworkResponse(
                    code = if (index == 0) 403 else 200,
                    serializedEvent = serializedEvent
                )
            }
        }.map { it.serializedEvent }.toSet()

        assertEquals(failedEvents, preferences.events)
    }

    @Test
    fun testClickEvent() {
        assertEquals(200, networkManager.sendEvent(Event.Click(eventParametersA)))
    }

    @Test
    fun testViewEvent() {
        assertEquals(200, networkManager.sendEvent(Event.View(eventParametersA)))
    }

    @Test
    fun testConversionEvent() {
        assertEquals(200, networkManager.sendEvent(Event.Conversion(eventParametersA)))
    }
}
