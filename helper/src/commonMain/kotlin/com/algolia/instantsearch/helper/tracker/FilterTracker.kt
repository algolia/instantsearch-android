@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.tracker.internal.FilterDataTracker
import com.algolia.instantsearch.helper.tracker.internal.InsightsScope
import com.algolia.instantsearch.helper.tracker.internal.TrackableSearcher
import com.algolia.instantsearch.insights.Insights
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet
import kotlinx.coroutines.CoroutineScope

/**
 * Tracker of filter events insights.
 */
public interface FilterTracker {

    /**
     * Coroutine scope in which all tracking operations are executed.
     */
    val trackerScope: CoroutineScope

    /**
     * Track a filter click event.
     *
     * @param filter filter to track
     * @param eventName custom event name, overrides the default event name
     */
    public fun <F : Filter> trackClick(filter: F, eventName: String? = null)

    /**
     * Track a filter view event.
     *
     * @param filter filter to track
     * @param eventName custom event name, overrides the default event name
     */
    public fun <F : Filter> trackView(filter: F, eventName: String? = null)

    /**
     * Track a filter conversion event.
     *
     * @param filter filter to track
     * @param eventName custom event name, overrides the default event name
     */
    public fun <F : Filter> trackConversion(filter: F, eventName: String? = null)

    /**
     * Track a facet click event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param eventName custom event name, overrides the default event name
     */
    public fun trackClick(facet: Facet, attribute: Attribute, eventName: String? = null)

    /**
     * Track a facet view event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param eventName custom event name, overrides the default event name
     */
    public fun trackView(facet: Facet, attribute: Attribute, eventName: String? = null)

    /**
     * Track a facet conversion event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param eventName custom event name, overrides the default event name
     */
    public fun trackConversion(facet: Facet, attribute: Attribute, eventName: String? = null)
}

/**
 * Creates a [FilterTracker] object.
 *
 * @param eventName default event name
 * @param searcher single index searcher
 * @param insights actual events handler
 * @param coroutineScope coroutine scope to execute tracking operations
 */
public fun FilterTracker(
    eventName: String,
    searcher: SearcherSingleIndex,
    insights: Insights,
    coroutineScope: CoroutineScope = InsightsScope()
): FilterTracker = FilterDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.SingleIndex(searcher),
    tracker = insights,
    trackerScope = coroutineScope
)

/**
 * Creates a [FilterTracker] object.
 *
 * @param eventName default event name
 * @param searcher multiple index searcher
 * @param pointer pointer to a specific index position
 * @param insights actual events handler
 * @param coroutineScope coroutine scope to execute tracking operations
 */
public fun FilterTracker(
    eventName: String,
    searcher: SearcherMultipleIndex,
    pointer: Int,
    insights: Insights,
    coroutineScope: CoroutineScope = InsightsScope()
): FilterTracker = FilterDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.MultiIndex(searcher, pointer),
    tracker = insights,
    trackerScope = coroutineScope
)
