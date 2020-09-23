package com.algolia.instantsearch.insights

import android.app.Application
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.algolia.instantsearch.insights.event.Event
import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.instantsearch.insights.internal.converter.ConverterEventToEventInternal
import com.algolia.instantsearch.insights.internal.database.DatabaseSharedPreferences
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import kotlin.test.assertTrue

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.Q])
class AndroidTestDatabaseSharedPreferences {

    private val context get() = ApplicationProvider.getApplicationContext<Application>()
    private val eventA = "EventA"
    private val eventB = "EventB"
    private val eventC = "EventC"
    private val indexName = "latency"
    private val queryId = "6de2f7eaa537fa93d8f8f05b927953b1"
    private val userToken = "foobarbaz"
    private val positions = listOf(1)
    private val objectIDs = EventObjects.IDs("54675051")
    private val timestamp = System.currentTimeMillis()
    private val eventClick = Event.Click(
        eventName = eventA,
        timestamp = timestamp,
        eventObjects = objectIDs,
        userToken = userToken,
        positions = positions,
        queryId = queryId
    ) to indexName
    private val eventConversion = Event.Conversion(
        eventName = eventB,
        userToken = userToken,
        timestamp = timestamp,
        eventObjects = objectIDs,
        queryId = queryId
    ) to indexName
    private val eventView = Event.View(
        eventName = eventC,
        timestamp = timestamp,
        eventObjects = objectIDs,
        queryId = queryId,
        userToken = userToken
    ) to indexName
    private val click = ConverterEventToEventInternal.convert(eventClick)
    private val view = ConverterEventToEventInternal.convert(eventView)
    private val conversion = ConverterEventToEventInternal.convert(eventConversion)

    @Test
    fun test() {
        val events = listOf(
            click,
            conversion
        )
        val database = DatabaseSharedPreferences(context, indexName)

        database.overwrite(events)

        assertTrue(database.read().containsAll(events))

        database.append(view)

        assertTrue(database.read().containsAll(events.plus(view)))
    }
}
