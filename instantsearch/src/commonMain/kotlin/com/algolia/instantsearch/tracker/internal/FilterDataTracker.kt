package com.algolia.instantsearch.tracker.internal

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.filter.state.toFilter
import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.tracker.FilterTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Tracker of filter events insights.
 */
internal class FilterDataTracker(
    override val eventName: String,
    override val trackableSearcher: TrackableSearcher<*>,
    override val tracker: FilterTrackable,
    override val trackerScope: CoroutineScope = InsightsScope(),
) : FilterTracker, InsightsTracker<FilterTrackable> {

    // region Filter tracking methods
    public override fun trackClick(filter: Filter.Facet, customEventName: String?) {
        trackerScope.launch {
            tracker.clickedFilters(
                eventName = customEventName ?: eventName,
                filters = listOf(filter)
            )
        }
    }

    public override fun trackView(filter: Filter.Facet, customEventName: String?) {
        trackerScope.launch {
            tracker.viewedFilters(
                eventName = customEventName ?: eventName,
                filters = listOf(filter)
            )
        }
    }

    public override fun trackConversion(filter: Filter.Facet, customEventName: String?) {
        trackerScope.launch {
            tracker.convertedFilters(
                eventName = customEventName ?: eventName,
                filters = listOf(filter)
            )
        }
    }
    // endregion

    // region Facet tracking methods
    public override fun trackClick(facet: FacetHits, attribute: String, customEventName: String?) {
        val filterFacet = facet.toFilter(attribute)
        trackClick(filter = filterFacet, customEventName = customEventName)
    }

    public override fun trackView(facet: FacetHits, attribute: String, customEventName: String?) {
        val filterFacet = facet.toFilter(attribute)
        trackView(filter = filterFacet, customEventName = customEventName)
    }

    public override fun trackConversion(facet: FacetHits, attribute: String, customEventName: String?) {
        val filterFacet = facet.toFilter(attribute)
        trackConversion(filter = filterFacet, customEventName = customEventName)
    }
    // endregion
}
