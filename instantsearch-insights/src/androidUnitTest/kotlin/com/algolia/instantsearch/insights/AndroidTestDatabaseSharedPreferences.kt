package com.algolia.instantsearch.insights

import android.app.Application
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.algolia.instantsearch.insights.internal.data.local.InsightsPrefsRepository
import com.algolia.instantsearch.insights.internal.extension.insightsSharedPreferences
import com.algolia.instantsearch.insights.internal.extension.randomUUID
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.insights.internal.data.local.mapper.FilterFacetMapper
import com.algolia.instantsearch.insights.internal.data.local.model.FilterFacetDO
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.P])
class AndroidTestDatabaseSharedPreferences {

    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val eventA = "EventA"
    private val eventB = "EventB"
    private val eventC = "EventC"
    private val indexName = "latency"
    private val queryId = "6de2f7eaa537fa93d8f8f05b927953b1"
    private val userToken = randomUUID()
    private val positions = listOf(1)
    private val objectIDs = listOf("54675051")
    private val timestamp = System.currentTimeMillis()
    private val facets = listOf(
        Filter.Facet(attribute = "attributeString", isNegated = true, score = 1, value = "value"),
        Filter.Facet(attribute = "attributeNum", isNegated = true, value = 1.0),
        Filter.Facet(attribute = "attributeBoolean", isNegated = false, value = true)
    )
    private val filterFacets: List<FilterFacetDO> = facets.map(FilterFacetMapper::map)
    private val eventClick = InsightsEventDO(
        eventType = InsightsEventDO.EventType.Click,
        indexName = indexName,
        eventName = eventA,
        timestamp = timestamp,
        objectIDs = objectIDs,
        userToken = userToken,
        positions = positions,
        queryID = queryId
    )
    private val eventConversion = InsightsEventDO(
        eventType = InsightsEventDO.EventType.Conversion,
        indexName = indexName,
        eventName = eventB,
        userToken = userToken,
        timestamp = timestamp,
        objectIDs = objectIDs,
        queryID = queryId
    )
    private val eventView = InsightsEventDO(
        eventType = InsightsEventDO.EventType.View,
        indexName = indexName,
        eventName = eventC,
        timestamp = timestamp,
        filters = filterFacets,
        queryID = queryId,
        userToken = userToken
    )

    @Test
    fun test() {
        val events = listOf(
            eventClick,
            eventConversion
        )

        val database = InsightsPrefsRepository(context.insightsSharedPreferences(indexName))

        database.overwrite(events)
        assertTrue(database.read().containsAll(events))

        database.append(eventView)
        assertTrue(database.read().containsAll(events.plus(eventView)))
    }
}
