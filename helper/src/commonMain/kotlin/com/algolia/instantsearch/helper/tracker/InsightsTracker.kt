package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.insights.Insights

public abstract class InsightsTracker(
    internal val eventName: String,
    internal val searcher: TrackableSearcher,
    internal val insights: Insights
) {

    public constructor(
        eventName: String,
        searcher: SearcherSingleIndex,
        insights: Insights
    ) : this(
        eventName = eventName,
        searcher = TrackableSearcher.SingleIndex(searcher),
        insights = insights
    )

    public constructor(
        eventName: String,
        searcher: SearcherMultipleIndex,
        pointer: Int,
        insights: Insights
    ) : this(
        eventName = eventName,
        searcher = TrackableSearcher.MultiIndex(searcher, pointer),
        insights = insights
    )

    companion object
}
