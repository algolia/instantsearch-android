@file:Suppress("FunctionName", "DEPRECATION")

package com.algolia.instantsearch.tracker

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.instantsearch.tracker.internal.FilterDataTracker
import com.algolia.instantsearch.tracker.internal.InsightsScope
import com.algolia.instantsearch.tracker.internal.TrackableSearcher
import kotlinx.coroutines.CoroutineScope

/**
 * Tracker of filter events insights.
 */
public interface FilterTracker {

    /**
     * Coroutine scope in which all tracking operations are executed.
     */
    public val trackerScope: CoroutineScope

    /**
     * Track a filter click event.
     *
     * @param filter filter to track
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackClick(filter: Filter.Facet, customEventName: String? = null)

    /**
     * Track a filter view event.
     *
     * @param filter filter to track
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackView(filter: Filter.Facet, customEventName: String? = null)

    /**
     * Track a filter conversion event.
     *
     * @param filter filter to track
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackConversion(filter: Filter.Facet, customEventName: String? = null)

    /**
     * Track a facet click event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackClick(facet: FacetHits, attribute: String, customEventName: String? = null)

    /**
     * Track a facet view event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackView(facet: FacetHits, attribute: String, customEventName: String? = null)

    /**
     * Track a facet conversion event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackConversion(facet: FacetHits, attribute: String, customEventName: String? = null)
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
    searcher: SearcherForHits<*>,
    insights: Insights,
    coroutineScope: CoroutineScope = InsightsScope(),
): FilterTracker = FilterDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.HitsSearcher(searcher),
    tracker = insights,
    trackerScope = coroutineScope
)
