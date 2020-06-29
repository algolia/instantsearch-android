package com.algolia.instantsearch.helper.tracker.internal

/**
 * Insights class wrapper with tracking capabilities.
 */
internal interface InsightsTracker<T> {
    public val eventName: String
    public val trackableSearcher: TrackableSearcher<*>
    public val tracker: T
}
