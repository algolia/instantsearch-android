package com.algolia.instantsearch.tracker.internal

import kotlinx.coroutines.CoroutineScope

/**
 * Insights class wrapper with tracking capabilities.
 */
internal interface InsightsTracker<T> {
    public val eventName: String
    public val trackableSearcher: TrackableSearcher<*>
    public val tracker: T
    public val trackerScope: CoroutineScope
}
