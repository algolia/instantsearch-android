package tracker

import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.instantsearch.tracker.internal.FilterDataTracker
import com.algolia.instantsearch.tracker.internal.TrackableSearcher
import com.algolia.instantsearch.filter.Filter
import com.algolia.search.model.search.Facet
import MainCoroutineRule
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import org.junit.Rule
import kotlin.test.Test
import kotlinx.coroutines.test.runTest

class TestFiltersTracker {

    @get:Rule
    val coroutineRule = MainCoroutineRule()
    private val testCoroutineScope = CoroutineScope(coroutineRule.testDispatcher)

    private val eventName = "eventName"
    private val trackableSearcher = mockk<TrackableSearcher<*>>()
    private val filterTrackable = mockk<FilterTrackable>(relaxed = true)
    private val filtersTracker = FilterDataTracker(eventName, trackableSearcher, filterTrackable, testCoroutineScope)

    @Test
    fun testTrackClick() = runTest {
        val eventName = "customEventName"
        val filter = Filter.Facet(attribute = "attribute", value = "value")

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
    fun testTrackClickFacet() = runTest {
        val eventName = "customEventName"
        val attribute = "attribute"
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
    fun testTrackConversion() = runTest {
        val eventName = "customEventName"
        val value = "value"
        val filter = Filter.Facet(attribute = "attribute", value = value)

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
        val eventName = "customEventName"
        val value = "value"
        val facet = Facet(value = value, count = 0)
        val filter = Filter.Facet(attribute = "attribute", value = value)

        filtersTracker.trackConversion(facet = facet, attribute = "attribute", customEventName = eventName)

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
        val eventName = "customEventName"
        val value = "value"
        val filter = Filter.Facet(attribute = "attribute", value = value)

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
        val eventName = "customEventName"
        val attribute = "attribute"
        val value = "value"
        val facet = Facet(value = value, count = 0)
        val filter = Filter.Facet(attribute = "attribute", value = value)

        filtersTracker.trackView(facet = facet, attribute = attribute, customEventName = eventName)

        verify {
            filterTrackable.viewedFilters(
                eventName = eventName,
                filters = listOf(filter),
                timestamp = any()
            )
        }
    }
}
