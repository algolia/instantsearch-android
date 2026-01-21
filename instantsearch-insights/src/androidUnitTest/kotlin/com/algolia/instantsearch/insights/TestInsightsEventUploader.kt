package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.insights.internal.extension.randomUUID
import com.algolia.instantsearch.insights.internal.uploader.InsightsEventUploader
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.insights.internal.data.local.mapper.FilterFacetMapper
import com.algolia.instantsearch.insights.internal.data.local.model.FilterFacetDO
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import kotlinx.serialization.json.JsonObject
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.test.runTest

public class TestInsightsEventUploader {

    @Test
    public fun testUploadAll(): Unit = runTest {
        val localRepository = TestInsightsLocalRepository()
        val distantRepository = TestInsightsDistantRepository()
        val uploader = InsightsEventUploader(localRepository, distantRepository)
        val eventA = "EventA"
        val eventB = "EventB"
        val eventC = "EventC"
        val eventD = "EventD"
        val indexName = "latency"
        val queryID = "6de2f7eaa537fa93d8f8f05b927953b1"
        val userToken = randomUUID()
        val positions = listOf(1)
        val objectIDs = listOf("54675051")
        val filters = listOf(Filter.Facet("foo", "bar"))
        val filterFacets: List<FilterFacetDO> = filters.map(FilterFacetMapper::map)
        val timeNow = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val eventClick = InsightsEventDO(
            eventType = InsightsEventDO.EventType.Click,
            eventName = eventA,
            indexName = indexName,
            userToken = userToken,
            timestamp = timeNow.toInstant().toEpochMilli(),
            queryID = queryID,
            objectIDs = objectIDs,
            positions = positions
        )
        val eventConversion = InsightsEventDO(
            eventType = InsightsEventDO.EventType.Conversion,
            eventName = eventB,
            indexName = indexName,
            userToken = userToken,
            timestamp = timeNow.minusHours(12).toInstant().toEpochMilli(),
            queryID = queryID,
            objectIDs = objectIDs
        )
        val eventView = InsightsEventDO(
            eventType = InsightsEventDO.EventType.View,
            eventName = eventC,
            indexName = indexName,
            userToken = userToken,
            timestamp = timeNow.minusDays(3).toInstant().toEpochMilli(),
            queryID = queryID,
            filters = filterFacets
        )
        val expiredEventClick = InsightsEventDO(
            eventType = InsightsEventDO.EventType.Click,
            eventName = eventD,
            indexName = indexName,
            userToken = userToken,
            timestamp = timeNow.minusDays(4).toInstant().toEpochMilli(),
            queryID = queryID,
            objectIDs = objectIDs,
            positions = positions
        )

        val events = listOf(eventClick, eventConversion, eventView, expiredEventClick)
        localRepository.overwrite(events)
        uploader.uploadAll()
        assertEquals(0, localRepository.insightsEvents.size)
        assertEquals(3, distantRepository.eventsSent.size)
        assertEquals(listOf(eventClick, eventConversion, eventView), distantRepository.eventsSent)
    }

    private class TestInsightsLocalRepository : InsightsLocalRepository {

        val insightsEvents = mutableListOf<InsightsEventDO>()

        override fun append(event: InsightsEventDO) {
            insightsEvents.add(event)
        }

        override fun overwrite(events: List<InsightsEventDO>) {
            insightsEvents.clear()
            insightsEvents.addAll(events)
        }

        override fun read(): List<InsightsEventDO> {
            return insightsEvents
        }

        override fun count(): Int {
            return insightsEvents.size
        }

        override fun clear() {
            insightsEvents.clear()
        }
    }

    private class TestInsightsDistantRepository : InsightsDistantRepository {
        var eventsSent = mutableListOf<InsightsEventDO>()

        override suspend fun customPost(
            path: String,
            parameters: Map<String, Any>?,
            body: JsonObject?,
            requestOptions: com.algolia.client.transport.RequestOptions?,
        ): JsonObject = JsonObject(emptyMap())

        override suspend fun send(event: InsightsEventDO): EventResponse {
            eventsSent += event
            return EventResponse(event, 200)
        }

        override val apiKey = "apiKey"
        override val applicationID = "applicationID"
    }
}
