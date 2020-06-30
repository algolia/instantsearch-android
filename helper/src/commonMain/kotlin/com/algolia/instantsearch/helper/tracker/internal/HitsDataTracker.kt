package com.algolia.instantsearch.helper.tracker.internal

import com.algolia.instantsearch.helper.tracker.HitsTracker
import com.algolia.instantsearch.insights.HitsAfterSearchTrackable
import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.search.model.QueryID
import com.algolia.search.model.indexing.Indexable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Tracker of hits events insights.
 */
internal class HitsDataTracker(
    override val eventName: String,
    override val trackableSearcher: TrackableSearcher<*>,
    override val tracker: HitsAfterSearchTrackable,
    override val coroutineScope: CoroutineScope
) : HitsTracker, InsightsTracker<HitsAfterSearchTrackable>, QueryIDContainer {

    public override var queryID: QueryID? = null

    init {
        trackableSearcher.setClickAnalyticsOn(true)
    }

    // region Hits tracking methods
    public override fun <T : Indexable> trackClick(hit: T, position: Int, customEventName: String?) {
        coroutineScope.launch {
            val id = queryID ?: return@launch
            tracker.clickedAfterSearch(
                eventName = customEventName ?: eventName,
                queryId = id.raw,
                objectIDs = EventObjects.IDs(hit.objectID.raw),
                positions = listOf(position)
            )
        }
    }

    public override fun <T : Indexable> trackConvert(hit: T, customEventName: String?) {
        coroutineScope.launch {
            val id = queryID ?: return@launch
            tracker.convertedAfterSearch(
                eventName = customEventName ?: eventName,
                queryId = id.raw,
                objectIDs = EventObjects.IDs(hit.objectID.raw)
            )
        }
    }

    public override fun <T : Indexable> trackView(hit: T, customEventName: String?) {
        coroutineScope.launch {
            tracker.viewed(
                eventName = customEventName ?: eventName,
                objectIDs = EventObjects.IDs(hit.objectID.raw)
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
