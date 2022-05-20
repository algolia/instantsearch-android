@file:Suppress("FunctionName", "DEPRECATION")

package com.algolia.instantsearch.tracker

import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.instantsearch.tracker.internal.FilterDataTracker
import com.algolia.instantsearch.tracker.internal.InsightsScope
import com.algolia.instantsearch.tracker.internal.TrackableSearcher
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName
import com.algolia.search.model.search.Facet
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
    public fun trackClick(filter: Filter.Facet, customEventName: EventName? = null)

    /**
     * Track a filter view event.
     *
     * @param filter filter to track
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackView(filter: Filter.Facet, customEventName: EventName? = null)

    /**
     * Track a filter conversion event.
     *
     * @param filter filter to track
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackConversion(filter: Filter.Facet, customEventName: EventName? = null)

    /**
     * Track a facet click event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackClick(facet: Facet, attribute: Attribute, customEventName: EventName? = null)

    /**
     * Track a facet view event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackView(facet: Facet, attribute: Attribute, customEventName: EventName? = null)

    /**
     * Track a facet conversion event.
     *
     * @param facet facet to track
     * @param attribute facet attribute
     * @param customEventName custom event name, overrides the default event name
     */
    public fun trackConversion(facet: Facet, attribute: Attribute, customEventName: EventName? = null)
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
    eventName: EventName,
    searcher: SearcherForHits<*>,
    insights: Insights,
    coroutineScope: CoroutineScope = InsightsScope(),
): FilterTracker = FilterDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.HitsSearcher(searcher),
    tracker = insights,
    trackerScope = coroutineScope
)
