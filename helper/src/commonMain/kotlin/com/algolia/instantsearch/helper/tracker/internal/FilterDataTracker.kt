package com.algolia.instantsearch.helper.tracker.internal

import com.algolia.instantsearch.helper.filter.state.toFilter
import com.algolia.instantsearch.helper.tracker.FilterTracker
import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterConverter
import com.algolia.search.model.search.Facet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Tracker of filter events insights.
 */
internal class FilterDataTracker(
    override val eventName: String,
    override val trackableSearcher: TrackableSearcher<*>,
    override val tracker: FilterTrackable,
    override val coroutineScope: CoroutineScope = InsightsScope()
) : FilterTracker, InsightsTracker<FilterTrackable> {

    // region Filter tracking methods
    public override fun <F : Filter> trackClick(filter: F, customEventName: String?) {
        coroutineScope.launch {
            val sqlForm = FilterConverter.SQL(filter)
            tracker.clicked(
                eventName = customEventName ?: eventName,
                filters = EventObjects.Filters(sqlForm)
            )
        }
    }

    public override fun <F : Filter> trackView(filter: F, customEventName: String?) {
        coroutineScope.launch {
            val sqlForm = FilterConverter.SQL(filter)
            tracker.viewed(
                eventName = customEventName ?: eventName,
                filters = EventObjects.Filters(sqlForm)
            )
        }
    }

    public override fun <F : Filter> trackConversion(filter: F, customEventName: String?) {
        coroutineScope.launch {
            val sqlForm = FilterConverter.SQL(filter)
            tracker.converted(
                eventName = customEventName ?: eventName,
                filters = EventObjects.Filters(sqlForm)
            )
        }
    }
    // endregion

    // region Facet tracking methods
    public override fun trackClick(facet: Facet, attribute: Attribute, customEventName: String?) {
        val filterFacet = facet.toFilter(attribute)
        trackClick(filter = filterFacet, customEventName = customEventName)
    }

    public override fun trackView(facet: Facet, attribute: Attribute, customEventName: String?) {
        val filterFacet = facet.toFilter(attribute)
        trackView(filter = filterFacet, customEventName = customEventName)
    }

    public override fun trackConversion(facet: Facet, attribute: Attribute, customEventName: String?) {
        val filterFacet = facet.toFilter(attribute)
        trackConversion(filter = filterFacet, customEventName = customEventName)
    }
    // endregion
}
