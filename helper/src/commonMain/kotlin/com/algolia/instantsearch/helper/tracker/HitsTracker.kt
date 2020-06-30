@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.tracker.internal.HitsDataTracker
import com.algolia.instantsearch.helper.tracker.internal.InsightsScope
import com.algolia.instantsearch.helper.tracker.internal.TrackableSearcher
import com.algolia.instantsearch.insights.Insights
import com.algolia.search.model.indexing.Indexable
import kotlinx.coroutines.CoroutineScope

/**
 * Tracker of hits events insights.
 */
public interface HitsTracker : Connection {

    /**
     * Coroutine scope in which all tracking operations are executed.
     */
    val trackerScope: CoroutineScope

    /**
     * Track a hit click event.
     *
     * @param hit hit to track
     * @param customEventName custom event name, overrides the default value.
     */
    public fun <T : Indexable> trackClick(hit: T, position: Int, customEventName: String? = null)

    /**
     * Track a hit convert event.
     *
     * @param hit hit to track
     * @param customEventName custom event name, overrides the default event name
     */
    public fun <T : Indexable> trackConvert(hit: T, customEventName: String? = null)

    /**
     * Track a hit view event.
     *
     * @param hit hit to track
     * @param customEventName custom event name, overrides the default event name
     */
    public fun <T : Indexable> trackView(hit: T, customEventName: String? = null)
}

/**
 * Creates a [HitsTracker] object.
 *
 * @param eventName default event name
 * @param searcher single index searcher
 * @param insights actual events handler
 */
public fun HitsTracker(
    eventName: String,
    searcher: SearcherSingleIndex,
    insights: Insights,
    coroutineScope: CoroutineScope = InsightsScope()
): HitsTracker = HitsDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.SingleIndex(searcher),
    tracker = insights,
    trackerScope = coroutineScope
)

/**
 * Creates a [HitsTracker] object.
 *
 * @param eventName default event name
 * @param searcher multiple index searcher
 * @param pointer pointer to a specific index position
 * @param insights actual events handler
 */
public fun HitsTracker(
    eventName: String,
    searcher: SearcherMultipleIndex,
    pointer: Int,
    insights: Insights,
    coroutineScope: CoroutineScope = InsightsScope()
): HitsTracker = HitsDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.MultiIndex(searcher, pointer),
    tracker = insights,
    trackerScope = coroutineScope
)
