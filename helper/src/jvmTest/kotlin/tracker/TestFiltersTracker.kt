package tracker

import com.algolia.instantsearch.helper.tracker.internal.FilterDataTracker
import com.algolia.instantsearch.helper.tracker.internal.TrackableSearcher
import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet
import extension.MainCoroutineRule
import extension.runBlocking
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import org.junit.Rule
import kotlin.test.Test

class TestFiltersTracker {

    @get:Rule
    val coroutineRule = MainCoroutineRule()
    private val testCoroutineScope = CoroutineScope(coroutineRule.testDispatcher)

    private val eventName = "eventName"
    private val trackableSearcher = mockk<TrackableSearcher<*>>()
    private val filterTrackable = mockk<FilterTrackable>(relaxed = true)
    private val filtersTracker = FilterDataTracker(eventName, trackableSearcher, filterTrackable, testCoroutineScope)

    @Test
    fun testTrackClick() = coroutineRule.runBlocking {
        val eventName = "customEventName"
        val attribute = "attribute"
        val value = "value"
        val filter = Filter.Facet(Attribute(attribute), value)
        val filterSQLForm = EventObjects.Filters("\"$attribute\":\"$value\"")

        filtersTracker.trackClick(filter = filter, customEventName = eventName)

        verify {
            filterTrackable.clicked(
                eventName = eventName,
                filters = filterSQLForm,
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackClickFacet() = coroutineRule.runBlocking {
        val eventName = "customEventName"
        val attribute = "attribute"
        val value = "value"
        val facet = Facet(value = value, count = 0)
        val filterSQLForm = EventObjects.Filters("\"$attribute\":\"$value\"")

        filtersTracker.trackClick(facet = facet, attribute = Attribute(attribute), customEventName = eventName)

        verify {
            filterTrackable.clicked(
                eventName = eventName,
                filters = filterSQLForm,
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackConversion() = coroutineRule.runBlocking {
        val eventName = "customEventName"
        val attribute = "attribute"
        val value = "value"
        val filter = Filter.Facet(Attribute(attribute), value)
        val filterSQLForm = EventObjects.Filters("\"$attribute\":\"$value\"")

        filtersTracker.trackConversion(filter = filter, customEventName = eventName)

        verify {
            filterTrackable.converted(
                eventName = eventName,
                filters = filterSQLForm,
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackConversionFacet() {
        val eventName = "customEventName"
        val attribute = "attribute"
        val value = "value"
        val facet = Facet(value = value, count = 0)
        val filterSQLForm = EventObjects.Filters("\"$attribute\":\"$value\"")

        filtersTracker.trackConversion(facet = facet, attribute = Attribute(attribute), customEventName = eventName)

        verify {
            filterTrackable.converted(
                eventName = eventName,
                filters = filterSQLForm,
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackView() {
        val eventName = "customEventName"
        val attribute = "attribute"
        val value = "value"
        val filter = Filter.Facet(Attribute(attribute), value)
        val filterSQLForm = EventObjects.Filters("\"$attribute\":\"$value\"")

        filtersTracker.trackView(filter = filter, customEventName = eventName)

        verify {
            filterTrackable.viewed(
                eventName = eventName,
                filters = filterSQLForm,
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackViewFacet() {
        val eventName = "customEventName"
        val attribute = "attribute"
        val value = "value"
        val facet = Facet(value = value, count = 0)
        val filterSQLForm = EventObjects.Filters("\"$attribute\":\"$value\"")

        filtersTracker.trackView(facet = facet, attribute = Attribute(attribute), customEventName = eventName)

        verify {
            filterTrackable.viewed(
                eventName = eventName,
                filters = filterSQLForm,
                timestamp = any()
            )
        }
    }
}
