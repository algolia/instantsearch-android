package com.algolia.instantsearch.insights

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.algolia.instantsearch.insights.event.EventUploader
import com.algolia.instantsearch.insights.internal.extension.uploadEvents
import com.algolia.instantsearch.insights.internal.webservice.WebService
import com.algolia.instantsearch.insights.internal.webservice.WebServiceHttp
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.QueryID
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.InsightsEvent
import com.algolia.search.model.insights.UserToken
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
internal class InsightsTest {

    private val responseOK = WebService.Response(null, 200)
    private val eventA = EventName("EventA")
    private val eventB = EventName("EventB")
    private val eventC = EventName("EventC")
    private val indexName = IndexName("latency")
    private val appId = System.getenv("ALGOLIA_APPLICATION_ID")
    private val apiKey = System.getenv("ALGOLIA_API_KEY")
    private val queryID = QueryID("6de2f7eaa537fa93d8f8f05b927953b1")
    private val userToken = UserToken("foobarbaz")
    private val positions = listOf(1)
    private val objectIDs = listOf(ObjectID("54675051"))
    private val resourcesObjectIDs = InsightsEvent.Resources.ObjectIDs(objectIDs)
    private val filters = listOf(Filter.Facet(Attribute("foo"), "bar"))
    private val resourcesFilters = InsightsEvent.Resources.Filters(filters)
    private val timestamp = System.currentTimeMillis()
    private val configuration = Insights.Configuration(
        connectTimeoutInMilliseconds = 5000,
        readTimeoutInMilliseconds = 5000
    )
    private val eventClick = InsightsEvent.Click(
        eventName = eventA,
        indexName = indexName,
        userToken = userToken,
        timestamp = timestamp,
        queryID = queryID,
        resources = resourcesObjectIDs,
        positions = positions
    )
    private val eventConversion = InsightsEvent.Conversion(
        eventName = eventB,
        indexName = indexName,
        userToken = userToken,
        timestamp = timestamp,
        resources = resourcesObjectIDs,
        queryID = queryID
    )
    private val eventView = InsightsEvent.View(
        eventName = eventC,
        indexName = indexName,
        userToken = userToken,
        timestamp = timestamp,
        resources = resourcesFilters,
        queryID = queryID
    )

    private val webService
        get() = WebServiceHttp(
            appId = appId,
            apiKey = apiKey,
            connectTimeoutInMilliseconds = configuration.connectTimeoutInMilliseconds,
            readTimeoutInMilliseconds = configuration.readTimeoutInMilliseconds
        )

    init {
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
    }

    @Test
    fun testClickEvent() {
        runBlocking {
            val response = webService.send(eventClick)
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun testViewEvent() {
        runBlocking {
            val response = webService.send(eventView)
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun testConversionEvent() {
        runBlocking {
            val response = webService.send(eventConversion)
            assertEquals(HttpStatusCode.OK, response.status)
        }
    }

    @Test
    fun testMethods() {
        val events = mutableListOf(eventClick, eventConversion, eventView)
        val database = MockDatabase(indexName, events)
        val webService = MockWebService()
        val uploader = object : AssertingEventUploader(events, webService, database) {
            override fun startOneTimeUpload() {
                val trackedEvents = database.read()
                assertEquals(5, trackedEvents.size, "Five events should have been tracked")
                assertTrue(
                    trackedEvents.contains(eventClick),
                    "The clicked event should have been tracked through clicked and clickedAfterSearch"
                )
                assertTrue(
                    trackedEvents.contains(eventClick),
                    "The converted event should have been tracked through converted and convertedAfterSearch"
                )
                assertTrue(trackedEvents.contains(eventClick),
                    "The viewed event should have been tracked through viewed")
            }
        }
        val insights =
            Insights(indexName, uploader, database, webService)
        insights.userToken = UserToken("foo") // TODO: git stash apply to use default UUID token
        insights.clickedObjectIDs(eventClick.eventName, objectIDs)
        insights.clickedObjectIDsAfterSearch(
            eventName = eventClick.eventName,
            queryID = eventClick.queryID!!,
            objectIDs = objectIDs,
            positions = eventClick.positions!!
        )
        insights.convertedObjectIDs(eventConversion.eventName, objectIDs)
        insights.convertedObjectIDsAfterSearch(
            eventName = eventConversion.eventName,
            queryID = eventConversion.queryID!!,
            objectIDs = objectIDs
        )
        insights.viewedFilters(eventView.eventName, filters)
    }

    @Test
    fun testEnabled() {
        val events = mutableListOf(eventClick, eventConversion, eventView)
        val database = MockDatabase(indexName, events)
        val webService = MockWebService()
        val uploader = object : AssertingEventUploader(events, webService, database) {
            override fun startOneTimeUpload() {
                val trackedEvents = database.read()
                assertFalse(trackedEvents.contains(eventClick), "The first event should have been ignored")
                assertTrue(trackedEvents.contains(eventConversion), "The second event should be uploaded")
            }
        }
        val insights =
            Insights(indexName, uploader, database, webService)
        insights.minBatchSize = 1 // Given an Insights that uploads every event

        insights.enabled = false // When a firstEvent is sent with insight disabled
        insights.clicked(eventClick)
        insights.enabled = true // And a secondEvent sent with insight enabled
        insights.converted(eventConversion)
    }

    @Test
    fun testMinBatchSize() {
        val events = mutableListOf(eventClick, eventConversion, eventView)
        val database = MockDatabase(indexName, events)
        val webService = MockWebService()
        val uploader = MinBatchSizeEventUploader(events, webService, database)
        val insights =
            Insights(indexName, uploader, database, webService)

        // Given a minBatchSize of one and one event
        insights.minBatchSize = 1
        insights.track(eventClick)
        // Given a minBatchSize of two and two events
        insights.minBatchSize = 2
        insights.track(eventClick)
        insights.track(eventClick)
        // Given a minBatchSize of four and four events
        insights.minBatchSize = 4
        insights.track(eventClick)
        insights.track(eventClick)
        insights.track(eventClick)
        insights.track(eventClick)
    }

    inner class MinBatchSizeEventUploader(
        events: MutableList<InsightsEvent>,
        webService: MockWebService,
        private val database: MockDatabase,
    ) : AssertingEventUploader(events, webService, database) {

        override fun startOneTimeUpload() {
            when (count) {
                // Expect a single event on first call
                0 -> assertEquals(1, database.count(), "startOneTimeUpload should be called first with one event")
                // Expect two events on second call
                1 -> assertEquals(2, database.count(), "startOneTimeUpload should be called second with two events")
                // Expect two events on third call
                2 -> assertEquals(4, database.count(), "startOneTimeUpload should be called third with four events")
            }
            count++
            database.clear()
        }
    }

    /**
     * Tests the integration of events, WebService and Database.
     */
    @Test
    fun testIntegration() {
        val events = mutableListOf(eventClick, eventConversion, eventView)
        val database = MockDatabase(indexName, events)
        val webService = MockWebService()
        val uploader = IntegrationEventUploader(events, webService, database)
        val insights = Insights(indexName, uploader, database, webService).apply {
            minBatchSize = 1
        }
        val errorHttpStatusCode = HttpStatusCode(value = -1, description = "error!")

        webService.code = HttpStatusCode.OK // Given a working web service
        insights.track(eventClick)
        webService.code = errorHttpStatusCode // Given a web service that errors
        insights.track(eventConversion)
        webService.code = HttpStatusCode.BadRequest // Given a working web service returning an HTTP error
        insights.track(eventView) // When tracking an event

        webService.code = errorHttpStatusCode// Given a web service that errors
        insights.userToken = userToken // Given an userToken

        // When adding events without explicitly-provided userToken
        insights.clickedObjectIDsAfterSearch(
            eventName = eventA,
            queryID = queryID,
            objectIDs = objectIDs,
            positions = positions,
            timestamp = timestamp
        )
        insights.clickedObjectIDs(
            eventName = eventA,
            timestamp = timestamp,
            objectIDs = objectIDs
        )
        insights.convertedObjectIDsAfterSearch(
            eventName = eventB,
            timestamp = timestamp,
            queryID = queryID,
            objectIDs = objectIDs
        )
        webService.code = HttpStatusCode.OK // Given a working web service
        insights.viewed(eventView)
    }

    inner class IntegrationEventUploader(
        events: MutableList<InsightsEvent>,
        private val webService: MockWebService,
        private val database: MockDatabase,
    ) : AssertingEventUploader(events, webService, database) {

        override fun startOneTimeUpload() {
            val clickEventNotForSearch = InsightsEvent.Click(
                eventName = eventA,
                indexName = indexName,
                userToken = userToken,
                timestamp = timestamp,
                resources = resourcesObjectIDs,
                positions = null // A Click event not for Search has no positions
            )

            when (count) {
                0 -> assertEquals(listOf(eventClick), database.read(), "failed 0") // expect added first
                1 -> assertEquals(listOf(eventConversion),
                    database.read(),
                    "failed 1") // expect flush then added second
                2 -> assertEquals(listOf(eventConversion, eventView), database.read(), "failed 2")

                3 -> assertEquals(listOf(eventClick), database.read(), "failed 3") // expect flush then added first
                4 -> assertEquals(listOf(eventClick, clickEventNotForSearch),
                    database.read(),
                    "failed 4") // expect added first
                5 -> assertEquals(
                    listOf(eventClick, clickEventNotForSearch, eventConversion),
                    database.read(),
                    "failed 5"
                ) // expect added second
                6 -> assertEquals(
                    listOf(eventClick, clickEventNotForSearch, eventConversion, eventView),
                    database.read(),
                    "failed 6"
                ) // expect added third
            }
            webService.uploadEvents(database, indexName)
            when (count) {
                0 -> assert(database.read().isEmpty()) // expect flushed first
                1 -> assertEquals(listOf(eventConversion), database.read()) // expect kept second
                2 -> assert(database.read().isEmpty()) // expect flushed events

                3 -> assertEquals(listOf(eventClick), database.read()) // expect kept first
                4 -> assertEquals(listOf(eventClick, clickEventNotForSearch), database.read()) // expect kept first2
                5 -> assertEquals(listOf(eventClick, clickEventNotForSearch, eventConversion),
                    database.read()) // expect kept second
                6 -> assert(database.read().isEmpty()) // expect flushed events
            }
            count++
        }
    }

    abstract inner class AssertingEventUploader(
        private val events: MutableList<InsightsEvent>,
        private val webService: MockWebService,
        private val database: MockDatabase,
    ) : EventUploader {

        protected var count: Int = 0

        override fun startPeriodicUpload() {
            assertEquals(events, database.read())
            webService.uploadEvents(database, indexName)
            assert(database.read().isEmpty())
        }
    }
}
