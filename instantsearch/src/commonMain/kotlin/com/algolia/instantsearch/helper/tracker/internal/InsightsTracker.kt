package com.algolia.instantsearch.helper.tracker.internal

import com.algolia.search.model.insights.EventName
import kotlinx.coroutines.CoroutineScope

/**
 * Insights class wrapper with tracking capabilities.
 */
internal interface InsightsTracker<T> {
    public val eventName: EventName
    public val trackableSearcher: TrackableSearcher<*>
    public val tracker: T
    public val trackerScope: CoroutineScope
}
