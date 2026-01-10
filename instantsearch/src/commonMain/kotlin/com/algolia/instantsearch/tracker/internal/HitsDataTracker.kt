package com.algolia.instantsearch.tracker.internal

import com.algolia.instantsearch.insights.HitsAfterSearchTrackable
import com.algolia.instantsearch.migration2to3.EventName
import com.algolia.instantsearch.migration2to3.Indexable
import com.algolia.instantsearch.migration2to3.QueryID
import com.algolia.instantsearch.tracker.HitsTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Tracker of hits events insights.
 */
internal class HitsDataTracker(
    override val eventName: EventName,
    override val trackableSearcher: TrackableSearcher<*>,
    override val tracker: HitsAfterSearchTrackable,
    override val trackerScope: CoroutineScope,
) : HitsTracker, InsightsTracker<HitsAfterSearchTrackable>, QueryIDContainer {

    public override var queryID: QueryID? = null

    init {
        trackableSearcher.setClickAnalyticsOn(true)
    }

    // region Hits tracking methods
    public override fun <T : Indexable> trackClick(hit: T, position: Int, customEventName: EventName?) {
        trackerScope.launch {
            val id = queryID ?: return@launch
            tracker.clickedObjectIDsAfterSearch(
                eventName = customEventName ?: eventName,
                queryID = id,
                objectIDs = listOf(hit.objectID),
                positions = listOf(position)
            )
        }
    }

    public override fun <T : Indexable> trackConvert(hit: T, customEventName: EventName?) {
        trackerScope.launch {
            val id = queryID ?: return@launch
            tracker.convertedObjectIDsAfterSearch(
                eventName = customEventName ?: eventName,
                queryID = id,
                objectIDs = listOf(hit.objectID)
            )
        }
    }

    public override fun <T : Indexable> trackView(hit: T, customEventName: EventName?) {
        trackerScope.launch {
            tracker.viewedObjectIDs(
                eventName = customEventName ?: eventName,
                objectIDs = listOf(hit.objectID)
            )
        }
    }
    // endregion

    override var isConnected: Boolean = false
        private set

    private var subscription: SubscriptionJob<*>? = null

    override fun connect() {
        subscription?.cancel()
        subscription = trackableSearcher.subscribeForQueryIDChange(this)
        isConnected = true
    }

    override fun disconnect() {
        subscription?.cancel()
        isConnected = false
    }
}
