package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.data.distant.InsightsDistantRepository
import com.algolia.instantsearch.insights.internal.data.local.InsightsLocalRepository
import com.algolia.instantsearch.insights.internal.event.EventResponse
import com.algolia.instantsearch.insights.internal.extension.randomUUID
import com.algolia.instantsearch.insights.internal.uploader.InsightsEventUploader
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.QueryID
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.InsightsEvent
import com.algolia.search.model.insights.UserToken
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals

public class TestInsightsEventUploader {

    @Test
    public fun testUploadAll(): Unit = runBlocking {
        val localRepository = TestInsightsLocalRepository()
        val distantRepository = TestInsightsDistantRepository()
        val uploader = InsightsEventUploader(localRepository, distantRepository)
        val eventA = EventName("EventA")
        val eventB = EventName("EventB")
        val eventC = EventName("EventC")
        val eventD = EventName("EventD")
        val indexName = IndexName("latency")
        val queryID = QueryID("6de2f7eaa537fa93d8f8f05b927953b1")
        val userToken = UserToken(randomUUID())
        val positions = listOf(1)
        val objectIDs = listOf(ObjectID("54675051"))
        val resourcesObjectIDs = InsightsEvent.Resources.ObjectIDs(objectIDs)
        val filters = listOf(Filter.Facet(Attribute("foo"), "bar"))
        val resourcesFilters = InsightsEvent.Resources.Filters(filters)
        val timeNow = LocalDateTime.now().atZone(ZoneId.systemDefault())
        val eventClick = InsightsEvent.Click(
            eventName = eventA,
            indexName = indexName,
            userToken = userToken,
            timestamp = timeNow.toInstant().toEpochMilli(),
            queryID = queryID,
            resources = resourcesObjectIDs,
            positions = positions
        )
        val eventConversion = InsightsEvent.Conversion(
            eventName = eventB,
            indexName = indexName,
            userToken = userToken,
            timestamp = timeNow.minusHours(12).toInstant().toEpochMilli(),
            resources = resourcesObjectIDs,
            queryID = queryID
        )
        val eventView = InsightsEvent.View(
            eventName = eventC,
            indexName = indexName,
            userToken = userToken,
            timestamp = timeNow.minusDays(3).toInstant().toEpochMilli(),
            resources = resourcesFilters,
            queryID = queryID
        )
        val expiredEventClick = InsightsEvent.Click(
            eventName = eventD,
            indexName = indexName,
            userToken = userToken,
            timestamp = timeNow.minusDays(4).toInstant().toEpochMilli(),
            queryID = queryID,
            resources = resourcesObjectIDs,
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

        val insightsEvents = mutableListOf<InsightsEvent>()

        override fun append(event: InsightsEvent) {
            insightsEvents.add(event)
        }

        override fun overwrite(events: List<InsightsEvent>) {
            insightsEvents.clear()
            insightsEvents.addAll(events)
        }

        override fun read(): List<InsightsEvent> {
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
        var eventsSent = mutableListOf<InsightsEvent>()
        override suspend fun send(event: InsightsEvent): EventResponse {
            eventsSent += event
            return EventResponse(event, 200)
        }
    }
}
