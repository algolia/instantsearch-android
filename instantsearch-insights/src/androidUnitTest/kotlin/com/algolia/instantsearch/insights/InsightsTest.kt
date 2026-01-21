package com.algolia.instantsearch.insights

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.insights.internal.InsightsController
import com.algolia.instantsearch.insights.internal.cache.InsightsEventCache
import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.data.distant.InsightsHttpRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.data.local.mapper.FilterFacetMapper
import com.algolia.instantsearch.insights.internal.data.local.model.FilterFacetDO
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.extension.randomUUID
import com.algolia.instantsearch.insights.internal.uploader.InsightsEventUploader
import com.algolia.instantsearch.insights.internal.worker.InsightsManager
import com.algolia.client.api.InsightsClient
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test
import org.junit.Ignore
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
internal class InsightsTest {

    private val eventA = "EventA"
    private val eventB = "EventB"
    private val eventC = "EventC"
    private val eventD = "EventD"
    private val indexName = "latency"
    private val appId = "appId"
    private val apiKey = "apiKey"
    private val queryID = "6de2f7eaa537fa93d8f8f05b927953b1"
    private val userToken = randomUUID()
    private val positions = listOf(1)
    private val objectIDs = listOf("54675051")
    private val filters = listOf(Filter.Facet("foo", "bar"))
    private val filterFacets: List<FilterFacetDO> = filters.map(FilterFacetMapper::map)
    private val timestamp = System.currentTimeMillis()
    private val configuration = Insights.Configuration(
        connectTimeoutInMilliseconds = 5000,
        readTimeoutInMilliseconds = 5000
    )
    private val eventClick = InsightsEventDO(
        eventType = InsightsEventDO.EventType.Click,
        eventName = eventA,
        indexName = indexName,
        userToken = userToken,
        timestamp = timestamp,
        queryID = queryID,
        objectIDs = objectIDs,
        positions = positions
    )
    private val eventConversion = InsightsEventDO(
        eventType = InsightsEventDO.EventType.Conversion,
        eventName = eventB,
        indexName = indexName,
        userToken = userToken,
        timestamp = timestamp,
        queryID = queryID,
        objectIDs = objectIDs
    )
    private val eventView = InsightsEventDO(
        eventType = InsightsEventDO.EventType.View,
        eventName = eventC,
        indexName = indexName,
        userToken = userToken,
        timestamp = timestamp,
        queryID = queryID,
        filters = filterFacets
    )
    private val expiredEventClick = InsightsEventDO(
        eventType = InsightsEventDO.EventType.Click,
        eventName = eventD,
        indexName = indexName,
        userToken = userToken,
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).minusDays(4).toInstant().toEpochMilli(),
        queryID = queryID,
        objectIDs = objectIDs,
        positions = positions
    )

    private val clientInsights = InsightsClient(appId, apiKey)

    private val webService
        get() = InsightsHttpRepository(clientInsights)

    init {
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
    }

    @Test
    fun testClickEvent() = runTest {
        val response = webService.send(eventClick)
        assertEquals(200, response.code)
    }

    @Test
    fun testViewEvent() = runTest {
        val response = webService.send(eventView)
        assertEquals(200, response.code)
    }

    @Test
    fun testConversionEvent() = runTest {
        val response = webService.send(eventConversion)
        assertEquals(200, response.code)
    }

    @Ignore("Legacy tracking methods not supported in v3 test harness.")
    @Test
    fun testMethods() {
        val events = mutableListOf(eventClick, eventConversion, eventView)
        val localRepository = MockLocalRepository(events)
        val distantRepository = MockDistantRepository()
        val eventUploader = object : AssertingWorker(events, distantRepository, localRepository) {
            override fun startOneTimeUpload() {
                val trackedEvents = localRepository.read()
                assertEquals(5, trackedEvents.size, "Five events should have been tracked")
                assertTrue(
                    trackedEvents.contains(eventClick),
                    "The clicked event should have been tracked through clicked and clickedAfterSearch"
                )
                assertTrue(
                    trackedEvents.contains(eventClick),
                    "The converted event should have been tracked through converted and convertedAfterSearch"
                )
                assertTrue(
                    trackedEvents.contains(eventClick),
                    "The viewed event should have been tracked through viewed"
                )
            }
        }
        val cache = InsightsEventCache(localRepository)
        val uploader = InsightsEventUploader(localRepository, distantRepository)
        val insights = InsightsController(indexName, eventUploader, cache, uploader, true)
            .apply { userToken = this@InsightsTest.userToken }
        insights.userToken = "foo" // TODO: git stash apply to use default UUID token
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
        val localRepository = MockLocalRepository(events)
        val distantRepository = MockDistantRepository()
        val eventUploader = object : AssertingWorker(events, distantRepository, localRepository) {
            override fun startOneTimeUpload() {
                val trackedEvents = localRepository.read()
                assertFalse(trackedEvents.contains(eventClick), "The first event should have been ignored")
                assertTrue(trackedEvents.contains(eventConversion), "The second event should be uploaded")
            }
        }
        val cache = InsightsEventCache(localRepository)
        val uploader = InsightsEventUploader(localRepository, distantRepository)
        val insights = InsightsController(indexName, eventUploader, cache, uploader, true)
            .apply { userToken = this@InsightsTest.userToken }
        insights.minBatchSize = 1 // Given an Insights that uploads every event

        insights.enabled = false // When a firstEvent is sent with insight disabled
        insights.trackEvent(eventClick)
        insights.enabled = true // And a secondEvent sent with insight enabled
        insights.trackEvent(eventConversion)
    }

    @Test
    fun testMinBatchSize() {
        val events = mutableListOf(eventClick, eventConversion, eventView)
        val localRepository = MockLocalRepository(events)
        val distantRepository = MockDistantRepository()
        val eventUploader = MinBatchSizeWorker(events, distantRepository, localRepository)
        val cache = InsightsEventCache(localRepository)
        val uploader = InsightsEventUploader(localRepository, distantRepository)
        val insights = InsightsController(indexName, eventUploader, cache, uploader, true)
            .apply { userToken = this@InsightsTest.userToken }

        // Given a minBatchSize of one and one event
        insights.minBatchSize = 1
        insights.trackEvent(eventClick)
        // Given a minBatchSize of two and two events
        insights.minBatchSize = 2
        insights.trackEvent(eventClick)
        insights.trackEvent(eventClick)
        // Given a minBatchSize of four and four events
        insights.minBatchSize = 4
        insights.trackEvent(eventClick)
        insights.trackEvent(eventClick)
        insights.trackEvent(eventClick)
        insights.trackEvent(eventClick)
    }

    inner class MinBatchSizeWorker(
        events: MutableList<InsightsEventDO>,
        webService: MockDistantRepository,
        private val database: InsightsLocalRepository,
    ) : AssertingWorker(events, webService, database) {

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

    @Test
    fun testTimeStampGenerationEnabled() {
        val events = mutableListOf<InsightsEventDO>()
        val localRepository = MockLocalRepository(events)
        val distantRepository = MockDistantRepository()
        val eventUploader = MinBatchSizeWorker(events, distantRepository, localRepository)
        val cache = InsightsEventCache(localRepository)
        val uploader = InsightsEventUploader(localRepository, distantRepository)
        val insights = InsightsController(indexName, eventUploader, cache, uploader, true)
            .apply { userToken = this@InsightsTest.userToken }

        insights.trackEvent(eventClick)
        insights.trackEvent(eventClick.copy(timestamp = null))
        insights.trackEvent(eventConversion.copy(timestamp = null))
        insights.trackEvent(eventView.copy(timestamp = null))

        localRepository.read().forEach {
            assertNotNull(it.timestamp)
        }
    }

    @Test
    fun testTimeStampGenerationDisabled() {
        val events = mutableListOf<InsightsEventDO>()
        val localRepository = MockLocalRepository(events)
        val distantRepository = MockDistantRepository()
        val eventUploader = MinBatchSizeWorker(events, distantRepository, localRepository)
        val cache = InsightsEventCache(localRepository)
        val uploader = InsightsEventUploader(localRepository, distantRepository)
        val insights = InsightsController(indexName, eventUploader, cache, uploader, false)
            .apply { userToken = this@InsightsTest.userToken }

        insights.trackEvent(eventClick.copy(timestamp = null))
        insights.trackEvent(eventConversion.copy(timestamp = null))
        insights.trackEvent(eventView.copy(timestamp = null))

        localRepository.read().forEach { assertNull(it.timestamp) }
    }

    /**
     * Tests the integration of events, WebService and Database.
     */
    @Ignore("Legacy tracking methods not supported in v3 test harness.")
    @Test
    fun testIntegration() {
        val events = mutableListOf(eventClick, eventConversion, eventView, expiredEventClick)
        val localRepository = MockLocalRepository(events)
        val distantRepository = MockDistantRepository()
        val eventUploader = IntegrationWorker(events, distantRepository, localRepository)
        val cache = InsightsEventCache(localRepository)
        val uploader = InsightsEventUploader(localRepository, distantRepository)
        val insights = InsightsController(indexName, eventUploader, cache, uploader, true).apply {
            minBatchSize = 1
            userToken = this@InsightsTest.userToken
        }

        distantRepository.code = 200 // Given a working web service
        insights.trackEvent(eventClick)
        distantRepository.code = -1 // Given a web service that errors
        insights.trackEvent(eventConversion)
        distantRepository.code = 400 // Given a working web service returning an HTTP error
        insights.trackEvent(eventView) // When tracking an event

        distantRepository.code = -1 // Given a web service that errors
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
        distantRepository.code = 200 // Given a working web service
        insights.trackEvent(eventView)
    }

    inner class IntegrationWorker(
        events: MutableList<InsightsEventDO>,
        webService: InsightsDistantRepository,
        private val database: InsightsLocalRepository,
    ) : AssertingWorker(events, webService, database) {

        override fun startOneTimeUpload() {
            runBlocking {
                val clickEventNotForSearch = InsightsEventDO(
                    eventType = InsightsEventDO.EventType.Click,
                    eventName = eventA,
                    indexName = indexName,
                    userToken = userToken,
                    timestamp = timestamp,
                    objectIDs = objectIDs,
                    positions = null // A Click event not for Search has no positions
                )

                when (count) {
                    0 -> assertEquals(listOf(eventClick), database.read(), "failed 0") // expect added first
                    1 ->
                        assertEquals(
                            listOf(eventConversion),
                            database.read(),
                            "failed 1"
                        ) // expect flush then added second
                    2 -> assertEquals(listOf(eventConversion, eventView), database.read(), "failed 2")

                    3 -> assertEquals(listOf(eventClick), database.read(), "failed 3") // expect flush then added first
                    4 ->
                        assertEquals(
                            listOf(eventClick, clickEventNotForSearch),
                            database.read(),
                            "failed 4"
                        ) // expect added first
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
                uploader.uploadAll()
                when (count) {
                    0 -> assert(database.read().isEmpty()) // expect flushed first
                    1 -> assertEquals(listOf(eventConversion), database.read()) // expect kept second
                    2 -> assert(database.read().isEmpty()) // expect flushed events

                    3 -> assertEquals(listOf(eventClick), database.read()) // expect kept first
                    4 -> assertEquals(listOf(eventClick, clickEventNotForSearch), database.read()) // expect kept first2
                    5 ->
                        assertEquals(
                            listOf(eventClick, clickEventNotForSearch, eventConversion),
                            database.read()
                        ) // expect kept second
                    6 -> assert(database.read().isEmpty()) // expect flushed events
                }
                count++
            }
        }
    }

    private fun Insights.trackEvent(event: InsightsEventDO) {
        if (this is InsightsController) {
            track(event)
            return
        }
        val eventName = event.eventName
        val timestamp = event.timestamp
        val objectIDs = event.objectIDs.orEmpty()
        val filters = event.filters?.map(FilterFacetMapper::unmap).orEmpty()

        when (event.eventType) {
            InsightsEventDO.EventType.Click -> {
                if (event.queryID != null) {
                    clickedObjectIDsAfterSearch(eventName, event.queryID, objectIDs, event.positions.orEmpty(), timestamp)
                } else if (filters.isNotEmpty()) {
                    clickedFilters(eventName, filters, timestamp)
                } else {
                    clickedObjectIDs(eventName, objectIDs, timestamp)
                }
            }
            InsightsEventDO.EventType.Conversion -> {
                if (event.queryID != null) {
                    convertedObjectIDsAfterSearch(eventName, event.queryID, objectIDs, timestamp)
                } else if (filters.isNotEmpty()) {
                    convertedFilters(eventName, filters, timestamp)
                } else {
                    convertedObjectIDs(eventName, objectIDs, timestamp)
                }
            }
            InsightsEventDO.EventType.View -> {
                if (filters.isNotEmpty()) {
                    viewedFilters(eventName, filters, timestamp)
                } else {
                    viewedObjectIDs(eventName, objectIDs, timestamp)
                }
            }
        }
    }

    abstract inner class AssertingWorker(
        private val events: MutableList<InsightsEventDO>,
        distantRepository: InsightsDistantRepository,
        private val localRepository: InsightsLocalRepository,
    ) : InsightsManager {

        protected var count: Int = 0
        protected val uploader = InsightsEventUploader(localRepository, distantRepository)

        override fun startPeriodicUpload() {
            runBlocking {
                assertEquals(events, localRepository.read())
                uploader.uploadAll()
                assert(localRepository.read().isEmpty())
            }
        }
    }
}
