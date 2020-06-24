@file:Suppress("FunctionName")

package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.insights.HitsAfterSearchTrackable
import com.algolia.instantsearch.insights.Insights
import com.algolia.search.model.indexing.Indexable

public interface HitsTracker: QueryIDContainer {

    public fun <T : Indexable> trackClick(hit: T, position: Int, customEventName: String? = null)

    public fun <T : Indexable> trackConvert(hit: T, customEventName: String? = null)

    public fun <T : Indexable> trackView(hit: T, customEventName: String? = null)
}

public fun HitsTracker(
    eventName: String,
    trackableSearcher: TrackableSearcher<*>,
    tracker: HitsAfterSearchTrackable
): HitsTracker = HitsDataTracker(eventName = eventName, trackableSearcher = trackableSearcher, tracker = tracker)

public fun HitsTracker(
    eventName: String,
    searcher: SearcherSingleIndex,
    insights: Insights
): HitsTracker = HitsDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.SingleIndex(searcher),
    tracker = insights
)

public fun HitsTracker(
    eventName: String,
    searcher: SearcherMultipleIndex,
    pointer: Int,
    insights: Insights
): HitsTracker = HitsDataTracker(
    eventName = eventName,
    trackableSearcher = TrackableSearcher.MultiIndex(searcher, pointer),
    tracker = insights
)
