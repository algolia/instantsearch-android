@file:Suppress("FunctionName", "DEPRECATION")

package com.algolia.instantsearch.tracker

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.migration2to3.EventName
import com.algolia.instantsearch.migration2to3.Indexable
import com.algolia.instantsearch.searcher.SearcherForHits
import com.algolia.instantsearch.tracker.internal.HitsDataTracker
import com.algolia.instantsearch.tracker.internal.InsightsScope
import com.algolia.instantsearch.tracker.internal.TrackableSearcher
import kotlinx.coroutines.CoroutineScope

/**
 * Tracker of hits events insights.
 */
public interface HitsTracker : Connection {

    /**
     * Coroutine scope in which all tracking operations are executed.
     */
    public val trackerScope: CoroutineScope

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
    searcher: SearcherForHits<*>,
    insights: Insights,
    coroutineScope: CoroutineScope = InsightsScope(),
): HitsTracker = HitsDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.HitsSearcher(searcher),
    tracker = insights,
    trackerScope = coroutineScope
)
