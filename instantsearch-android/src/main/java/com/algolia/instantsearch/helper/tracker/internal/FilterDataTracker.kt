package com.algolia.instantsearch.helper.tracker.internal

import com.algolia.instantsearch.helper.filter.state.toFilter
import com.algolia.instantsearch.helper.tracker.FilterTracker
import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.search.Facet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Tracker of filter events insights.
 */
internal class FilterDataTracker(
    override val eventName: EventName,
    override val trackableSearcher: TrackableSearcher<*>,
    override val tracker: FilterTrackable,
    override val trackerScope: CoroutineScope = InsightsScope(),
) : FilterTracker, InsightsTracker<FilterTrackable> {

    // region Filter tracking methods
    public override fun trackClick(filter: Filter.Facet, customEventName: EventName?) {
        trackerScope.launch {
            tracker.clickedFilters(
                eventName = customEventName ?: eventName,
                filters = listOf(filter)
            )
        }
    }

    public override fun trackView(filter: Filter.Facet, customEventName: EventName?) {
        trackerScope.launch {
            tracker.viewedFilters(
                eventName = customEventName ?: eventName,
                filters = listOf(filter)
            )
        }
    }

    public override fun trackConversion(filter: Filter.Facet, customEventName: EventName?) {
        trackerScope.launch {
            tracker.convertedFilters(
                eventName = customEventName ?: eventName,
                filters = listOf(filter)
            )
        }
    }
    // endregion

    // region Facet tracking methods
    public override fun trackClick(facet: Facet, attribute: Attribute, customEventName: EventName?) {
        val filterFacet = facet.toFilter(attribute)
        trackClick(filter = filterFacet, customEventName = customEventName)
    }

    public override fun trackView(facet: Facet, attribute: Attribute, customEventName: EventName?) {
        val filterFacet = facet.toFilter(attribute)
        trackView(filter = filterFacet, customEventName = customEventName)
    }

    public override fun trackConversion(facet: Facet, attribute: Attribute, customEventName: EventName?) {
        val filterFacet = facet.toFilter(attribute)
        trackConversion(filter = filterFacet, customEventName = customEventName)
    }
    // endregion
}
