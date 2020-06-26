package com.algolia.instantsearch.helper.tracker.internal

import com.algolia.instantsearch.helper.tracker.TrackableSearcher

/**
 * Insights class wrapper with tracking capabilities.
 */
internal abstract class InsightsTracker<T>(
    internal val eventName: String,
    internal val trackableSearcher: TrackableSearcher<*>,
    internal val tracker: T
)
