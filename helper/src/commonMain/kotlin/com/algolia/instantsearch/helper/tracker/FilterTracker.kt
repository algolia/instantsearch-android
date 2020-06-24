@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.instantsearch.insights.Insights
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet

public interface FilterTracker {

    public fun <F : Filter> trackClick(filter: F, customEventName: String? = null)

    public fun <F : Filter> trackView(filter: F, customEventName: String? = null)

    public fun <F : Filter> trackConversion(filter: F, customEventName: String? = null)

    public fun trackClick(facet: Facet, attribute: Attribute, customEventName: String? = null)

    public fun trackView(facet: Facet, attribute: Attribute, customEventName: String? = null)

    public fun trackConversion(facet: Facet, attribute: Attribute, customEventName: String? = null)
}

public fun FilterTracker(
    eventName: String,
    trackableSearcher: TrackableSearcher<*>,
    tracker: FilterTrackable
): FilterTracker = FilterDataTracker(
    eventName = eventName,
    trackableSearcher = trackableSearcher,
    tracker = tracker
)

public fun FilterTracker(
    eventName: String,
    searcher: SearcherSingleIndex,
    insights: Insights
): FilterTracker = FilterDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.SingleIndex(searcher),
    tracker = insights
)

public fun FilterTracker(
    eventName: String,
    searcher: SearcherMultipleIndex,
    pointer: Int,
    insights: Insights
): FilterTracker = FilterDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.MultiIndex(searcher, pointer),
    tracker = insights
)
