package com.algolia.instantsearch.insights

import android.app.Application
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.algolia.instantsearch.insights.internal.database.DatabaseSharedPreferences
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.ObjectID
import com.algolia.search.model.QueryID
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.insights.InsightsEvent
import com.algolia.search.model.insights.UserToken
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class AndroidTestDatabaseSharedPreferences {

    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val eventA = EventName("EventA")
    private val eventB = EventName("EventB")
    private val eventC = EventName("EventC")
    private val indexName = IndexName("latency")
    private val queryId = QueryID("6de2f7eaa537fa93d8f8f05b927953b1")
    private val userToken = UserToken("foobarbaz")
    private val positions = listOf(1)
    private val objectIDs = listOf(ObjectID("54675051"))
    private val timestamp = System.currentTimeMillis()
    private val facets = listOf(
        Filter.Facet(attribute = Attribute("attributeString"), isNegated = true, score = 1, value = "value"),
        Filter.Facet(attribute = Attribute("attributeNum"), isNegated = true, value = 1),
        Filter.Facet(attribute = Attribute("attributeBoolean"), isNegated = false, value = true)
    )
    private val eventClick = InsightsEvent.Click(
        indexName = indexName,
        eventName = eventA,
        timestamp = timestamp,
        resources = InsightsEvent.Resources.ObjectIDs(objectIDs),
        userToken = userToken,
        positions = positions,
        queryID = queryId
    )
    private val eventConversion = InsightsEvent.Conversion(
        indexName = indexName,
        eventName = eventB,
        userToken = userToken,
        timestamp = timestamp,
        resources = InsightsEvent.Resources.ObjectIDs(objectIDs),
        queryID = queryId
    )
    private val eventView = InsightsEvent.View(
        indexName = indexName,
        eventName = eventC,
        timestamp = timestamp,
        resources = InsightsEvent.Resources.Filters(facets),
        queryID = queryId,
        userToken = userToken
    )

    @Test
    fun test() {
        val events = listOf(
            eventClick,
            eventConversion
        )
        val database = DatabaseSharedPreferences(context, indexName)

        database.overwrite(events)
        assertTrue(database.read().containsAll(events))

        database.append(eventView)
        assertTrue(database.read().containsAll(events.plus(eventView)))
    }
}
