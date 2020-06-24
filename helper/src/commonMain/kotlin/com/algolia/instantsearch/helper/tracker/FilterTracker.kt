@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.instantsearch.insights.Insights
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet

/**
 * Tracker of filter events insights.
 */
public interface FilterTracker {

    /**
     * Track a filter click event.
     *
     * @param filter filter to track
     * @param customEventName custom event name, overrides the default event name
     */
    public fun <F : Filter> trackClick(filter: F, customEventName: String? = null)

    /**
     * Track a filter view event.
     *
     * @param filter filter to track
     * @param customEventName custom event name, overrides the default event name
     */
    public fun <F : Filter> trackView(filter: F, customEventName: String? = null)

    /**
     * Track a filter conversion event.
     *
     * @param filter filter to track
     * @param customEventName custom event name, overrides the default event name
     */
    public fun <F : Filter> trackConversion(filter: F, customEventName: String? = null)

    /**
     * Track a facet click event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackClick(facet: Facet, attribute: Attribute, customEventName: String? = null)

    /**
     * Track a facet view event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackView(facet: Facet, attribute: Attribute, customEventName: String? = null)

    /**
     * Track a facet conversion event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackConversion(facet: Facet, attribute: Attribute, customEventName: String? = null)
}

/**
 * Creates a [FilterTracker] object.
 *
 * @param eventName default event name
 * @param trackableSearcher searcher wrapper with tracking capabilities enabled
 * @param tracker actual events handler
 */
public fun FilterTracker(
    eventName: String,
    trackableSearcher: TrackableSearcher<*>,
    tracker: FilterTrackable
): FilterTracker = FilterDataTracker(
    eventName = eventName,
    trackableSearcher = trackableSearcher,
    tracker = tracker
)

/**
 * Creates a [FilterTracker] object.
 *
 * @param eventName default event name
 * @param searcher single index searcher
 * @param insights actual events handler
 */
public fun FilterTracker(
    eventName: String,
    searcher: SearcherSingleIndex,
    insights: Insights
): FilterTracker = FilterDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.SingleIndex(searcher),
    tracker = insights
)

/**
 * Creates a [FilterTracker] object.
 *
 * @param eventName default event name
 * @param searcher multiple index searcher
 * @param pointer pointer to a specific index position
 * @param insights actual events handler
 */
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
