package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterConverter
import com.algolia.search.model.search.Facet

class FilterTracker(
    eventName: String,
    searcher: TrackableSearcher,
    insights: Insights
) : InsightsTracker(eventName, searcher, insights) {

    private val tracker: FilterTrackable
        get() = insights

    // region Filter tracking methods
    fun <F : Filter> trackClick(filter: F, customEventName: String? = null) {
        val sqlForm = FilterConverter.SQL(filter)
        tracker.clicked(
            eventName = customEventName ?: eventName,
            filters = EventObjects.Filters(sqlForm)
        )
    }

    fun <F : Filter> trackView(filter: F, customEventName: String? = null) {
        val sqlForm = FilterConverter.SQL(filter)
        tracker.viewed(
            eventName = customEventName ?: eventName,
            filters = EventObjects.Filters(sqlForm)
        )
    }

    fun <F : Filter> trackConversion(filter: F, customEventName: String? = null) {
        val sqlForm = FilterConverter.SQL(filter)
        tracker.converted(
            eventName = customEventName ?: eventName,
            filters = EventObjects.Filters(sqlForm)
        )
    }
    // endregion

    // region Facet tracking methods
    fun trackClick(facet: Facet, attribute: Attribute, customEventName: String? = null) {
        val filterFacet = filter(facet, attribute);
        trackClick(filter = filterFacet, customEventName = customEventName)
    }

    fun trackView(facet: Facet, attribute: Attribute, customEventName: String? = null) {
        val filterFacet = filter(facet, attribute);
        trackView(filter = filterFacet, customEventName = customEventName)
    }

    fun trackConversion(facet: Facet, attribute: Attribute, customEventName: String? = null) {
        val filterFacet = filter(facet, attribute);
        trackConversion(filter = filterFacet, customEventName = customEventName)
    }

    private fun filter(facet: Facet, attribute: Attribute): Filter.Facet {
        return Filter.Facet(attribute = attribute, value = facet.value)
    }
    // endregion

    companion object
}
