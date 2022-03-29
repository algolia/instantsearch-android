package tracker

import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.instantsearch.tracker.internal.FilterDataTracker
import com.algolia.instantsearch.tracker.internal.TrackableSearcher
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
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

    private val eventName = EventName("eventName")
    private val trackableSearcher = mockk<TrackableSearcher<*>>()
    private val filterTrackable = mockk<FilterTrackable>(relaxed = true)
    private val filtersTracker = FilterDataTracker(eventName, trackableSearcher, filterTrackable, testCoroutineScope)

    @Test
    fun testTrackClick() = coroutineRule.runBlocking {
        val eventName = EventName("customEventName")
        val filter = Filter.Facet(attribute = Attribute("attribute"), value = "value")

        filtersTracker.trackClick(filter = filter, customEventName = eventName)

        verify {
            filterTrackable.clickedFilters(
                eventName = eventName,
                filters = listOf(filter),
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackClickFacet() = coroutineRule.runBlocking {
        val eventName = EventName("customEventName")
        val attribute = Attribute("attribute")
        val value = "value"
        val facet = Facet(value = value, count = 0)
        val filter = Filter.Facet(attribute = attribute, value = value)

        filtersTracker.trackClick(facet = facet, attribute = attribute, customEventName = eventName)

        verify {
            filterTrackable.clickedFilters(
                eventName = eventName,
                filters = listOf(filter),
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackConversion() = coroutineRule.runBlocking {
        val eventName = EventName("customEventName")
        val value = "value"
        val filter = Filter.Facet(attribute = Attribute("attribute"), value = value)

        filtersTracker.trackConversion(filter = filter, customEventName = eventName)

        verify {
            filterTrackable.convertedFilters(
                eventName = eventName,
                filters = listOf(filter),
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackConversionFacet() {
        val eventName = EventName("customEventName")
        val value = "value"
        val facet = Facet(value = value, count = 0)
        val filter = Filter.Facet(attribute = Attribute("attribute"), value = value)

        filtersTracker.trackConversion(facet = facet, attribute = Attribute("attribute"), customEventName = eventName)

        verify {
            filterTrackable.convertedFilters(
                eventName = eventName,
                filters = listOf(filter),
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackView() {
        val eventName = EventName("customEventName")
        val value = "value"
        val filter = Filter.Facet(attribute = Attribute("attribute"), value = value)

        filtersTracker.trackView(filter = filter, customEventName = eventName)

        verify {
            filterTrackable.viewedFilters(
                eventName = eventName,
                filters = listOf(filter),
                timestamp = any()
            )
        }
    }

    @Test
    fun testTrackViewFacet() {
        val eventName = EventName("customEventName")
        val attribute = "attribute"
        val value = "value"
        val facet = Facet(value = value, count = 0)
        val filter = Filter.Facet(attribute = Attribute("attribute"), value = value)

        filtersTracker.trackView(facet = facet, attribute = Attribute(attribute), customEventName = eventName)

        verify {
            filterTrackable.viewedFilters(
                eventName = eventName,
                filters = listOf(filter),
                timestamp = any()
            )
        }
    }
}
