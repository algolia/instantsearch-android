package com.algolia.instantsearch.tracker.internal

import com.algolia.instantsearch.insights.HitsAfterSearchTrackable
import com.algolia.instantsearch.core.Indexable
import com.algolia.instantsearch.tracker.HitsTracker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Tracker of hits events insights.
 */
internal class HitsDataTracker(
    override val eventName: String,
    override val trackableSearcher: TrackableSearcher<*>,
    override val tracker: HitsAfterSearchTrackable,
    override val trackerScope: CoroutineScope,
) : HitsTracker, InsightsTracker<HitsAfterSearchTrackable>, QueryIDContainer {

    override var queryID: String? = null

    init {
        trackableSearcher.setClickAnalyticsOn(true)
    }

    // region Hits tracking methods
    override fun <T : Indexable> trackClick(hit: T, position: Int, customEventName: String?) {
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

    override fun <T : Indexable> trackConvert(hit: T, customEventName: String?) {
        trackerScope.launch {
            val id = queryID ?: return@launch
            tracker.convertedObjectIDsAfterSearch(
                eventName = customEventName ?: eventName,
                queryID = id,
                objectIDs = listOf(hit.objectID)
            )
        }
    }

    override fun <T : Indexable> trackView(hit: T, customEventName: String?) {
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
