package com.algolia.instantsearch.insights

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.insights.internal.InsightsController
import com.algolia.instantsearch.insights.internal.cache.InsightsEventCache
import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.data.distant.InsightsHttpRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.extension.randomUUID
import com.algolia.instantsearch.insights.internal.uploader.InsightsEventUploader
import com.algolia.instantsearch.insights.internal.worker.InsightsManager
import com.algolia.search.client.ClientInsights
import com.algolia.search.configuration.ConfigurationInsights
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.QueryID
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.InsightsEvent
import com.algolia.search.model.insights.UserToken
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test
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
    private val appId = requireNotNull(System.getenv("ALGOLIA_APPLICATION_ID"))
    private val apiKey = requireNotNull(System.getenv("ALGOLIA_API_KEY"))
    private val queryID = "6de2f7eaa537fa93d8f8f05b927953b1"
    private val userToken = randomUUID()
    private val positions = listOf(1)
    private val objectIDs = listOf("54675051")
    private val resourcesObjectIDs = com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.Resources.ObjectIDs(objectIDs)
    private val filters = listOf(Filter.Facet("foo", "bar"))
    private val resourcesFilters = com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.Resources.Filters(filters)
    private val timestamp = System.currentTimeMillis()
    private val configuration = Insights.Configuration(
        connectTimeoutInMilliseconds = 5000,
        readTimeoutInMilliseconds = 5000
    )
    private val eventClick = com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.Click(
        eventName = eventA,
        indexName = indexName,
        userToken = userToken,
        timestamp = timestamp,
        queryID = queryID,
        resources = resourcesObjectIDs,
        positions = positions
    )
    private val eventConversion = com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.Conversion(
        eventName = eventB,
        indexName = indexName,
        userToken = userToken,
        timestamp = timestamp,
        resources = resourcesObjectIDs,
        queryID = queryID
    )
    private val eventView = com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.View(
        eventName = eventC,
        indexName = indexName,
        userToken = userToken,
        timestamp = timestamp,
        resources = resourcesFilters,
        queryID = queryID
    )
    private val expiredEventClick = com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.Click(
        eventName = eventD,
        indexName = indexName,
        userToken = userToken,
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).minusDays(4).toInstant().toEpochMilli(),
        queryID = queryID,
        resources = resourcesObjectIDs,
        positions = positions
    )

    private val clientInsights = ClientInsights(
        ConfigurationInsights(
            applicationID = ApplicationID(appId),
            apiKey = APIKey(apiKey),
            writeTimeout = configuration.connectTimeoutInMilliseconds,
            readTimeout = configuration.readTimeoutInMilliseconds
        )
    )

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
        insights.minBatchSize = 1 // Given an Insights that uploads every event

        insights.enabled = false // When a firstEvent is sent with insight disabled
        insights.clicked(eventClick)
        insights.enabled = true // And a secondEvent sent with insight enabled
        insights.converted(eventConversion)
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

    inner class MinBatchSizeWorker(
        events: MutableList<InsightsEvent>,
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
        val events = mutableListOf<InsightsEvent>()
        val localRepository = MockLocalRepository(events)
        val distantRepository = MockDistantRepository()
        val eventUploader = MinBatchSizeWorker(events, distantRepository, localRepository)
        val cache = InsightsEventCache(localRepository)
        val uploader = InsightsEventUploader(localRepository, distantRepository)
        val insights = InsightsController(indexName, eventUploader, cache, uploader, true)

        insights.clicked(eventClick)
        insights.clicked(eventClick.copy(timestamp = null))
        insights.converted(eventConversion.copy(timestamp = null))
        insights.viewed(eventView.copy(timestamp = null))

        localRepository.read().forEach {
            assertNotNull(it.timestamp)
        }
    }

    @Test
    fun testTimeStampGenerationDisabled() {
        val events = mutableListOf<InsightsEvent>()
        val localRepository = MockLocalRepository(events)
        val distantRepository = MockDistantRepository()
        val eventUploader = MinBatchSizeWorker(events, distantRepository, localRepository)
        val cache = InsightsEventCache(localRepository)
        val uploader = InsightsEventUploader(localRepository, distantRepository)
        val insights = InsightsController(indexName, eventUploader, cache, uploader, false)

        insights.clicked(eventClick.copy(timestamp = null))
        insights.converted(eventConversion.copy(timestamp = null))
        insights.viewed(eventView.copy(timestamp = null))

        localRepository.read().forEach { assertNull(it.timestamp) }
    }

    /**
     * Tests the integration of events, WebService and Database.
     */
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
        }

        distantRepository.code = 200 // Given a working web service
        insights.track(eventClick)
        distantRepository.code = -1 // Given a web service that errors
        insights.track(eventConversion)
        distantRepository.code = 400 // Given a working web service returning an HTTP error
        insights.track(eventView) // When tracking an event

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
        insights.viewed(eventView)
    }

    inner class IntegrationWorker(
        events: MutableList<InsightsEvent>,
        webService: InsightsDistantRepository,
        private val database: InsightsLocalRepository,
    ) : AssertingWorker(events, webService, database) {

        override fun startOneTimeUpload() {
            runBlocking {
                val clickEventNotForSearch = com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.Click(
                    eventName = eventA,
                    indexName = indexName,
                    userToken = userToken,
                    timestamp = timestamp,
                    resources = resourcesObjectIDs,
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

    abstract inner class AssertingWorker(
        private val events: MutableList<InsightsEvent>,
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
