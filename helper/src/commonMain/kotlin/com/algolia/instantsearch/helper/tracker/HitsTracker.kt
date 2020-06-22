package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.insights.HitsAfterSearchTrackable
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.search.model.indexing.Indexable

public class HitsTracker(
    eventName: String,
    searcher: TrackableSearcher,
    insights: Insights
) : InsightsTracker(eventName, searcher, insights), QueryIDContainer { // TODO: better approach for tracker

    override var queryID: String? = null
    private val tracker: HitsAfterSearchTrackable
        get() = insights

    init {
        searcher.subscribeForQueryIDChange(this) // TODO: connector
        searcher.setClickAnalyticsOn(true)
    }

    // region Hits tracking methods
    fun <T : Indexable> trackClick(hit: T, position: Int, customEventName: String? = null) {
        val id = queryID ?: return
        tracker.clickedAfterSearch(
            eventName = customEventName ?: eventName,
            queryId = id,
            objectIDs = EventObjects.IDs(hit.objectID.raw),
            positions = listOf(position)
        )
    }

    fun <T : Indexable> trackConvert(hit: T, customEventName: String? = null) {
        val id = queryID ?: return
        tracker.convertedAfterSearch(
            eventName = customEventName ?: eventName,
            queryId = id,
            objectIDs = EventObjects.IDs(hit.objectID.raw)
        )
    }

    fun <T : Indexable> trackView(hit: T, customEventName: String? = null) {
        tracker.viewed(
            eventName = customEventName ?: eventName,
            objectIDs = EventObjects.IDs(hit.objectID.raw)
        )
    }
    // endregion

    companion object
}
