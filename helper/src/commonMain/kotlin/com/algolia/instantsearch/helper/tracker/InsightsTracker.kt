package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.helper.filter.state.toFilter
import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.instantsearch.insights.HitsAfterSearchTrackable
import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.search.model.Attribute
import com.algolia.search.model.QueryID
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterConverter
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.search.Facet

/**
 * A tracker for the Insights operations.
 */
public sealed class InsightsTracker<T>(
    internal val eventName: String,
    internal val trackableSearcher: TrackableSearcher<*>,
    internal val tracker: T
)

/**
 * Tracker of hits events insights.
 */
public class HitsDataTracker internal constructor(
    eventName: String,
    trackableSearcher: TrackableSearcher<*>,
    tracker: HitsAfterSearchTrackable
) : HitsTracker, InsightsTracker<HitsAfterSearchTrackable>(eventName, trackableSearcher, tracker), Connection {

    public override var queryID: QueryID? = null

    init {
        trackableSearcher.setClickAnalyticsOn(true)
    }

    // region Hits tracking methods
    public override fun <T : Indexable> trackClick(hit: T, position: Int, customEventName: String?) {
        val id = queryID ?: return
        tracker.clickedAfterSearch(
            eventName = customEventName ?: eventName,
            queryId = id.raw,
            objectIDs = EventObjects.IDs(hit.objectID.raw),
            positions = listOf(position)
        )
    }

    public override fun <T : Indexable> trackConvert(hit: T, customEventName: String?) {
        val id = queryID ?: return
        tracker.convertedAfterSearch(
            eventName = customEventName ?: eventName,
            queryId = id.raw,
            objectIDs = EventObjects.IDs(hit.objectID.raw)
        )
    }

    public override fun <T : Indexable> trackView(hit: T, customEventName: String?) {
        tracker.viewed(
            eventName = customEventName ?: eventName,
            objectIDs = EventObjects.IDs(hit.objectID.raw)
        )
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

/**
 * Tracker of filter events insights.
 */
public class FilterDataTracker internal constructor(
    eventName: String,
    trackableSearcher: TrackableSearcher<*>,
    tracker: FilterTrackable
) : FilterTracker, InsightsTracker<FilterTrackable>(eventName, trackableSearcher, tracker) {

    // region Filter tracking methods
    public override fun <F : Filter> trackClick(filter: F, customEventName: String?) {
        val sqlForm = FilterConverter.SQL(filter)
        tracker.clicked(
            eventName = customEventName ?: eventName,
            filters = EventObjects.Filters(sqlForm)
        )
    }

    public override fun <F : Filter> trackView(filter: F, customEventName: String?) {
        val sqlForm = FilterConverter.SQL(filter)
        tracker.viewed(
            eventName = customEventName ?: eventName,
            filters = EventObjects.Filters(sqlForm)
        )
    }

    public override fun <F : Filter> trackConversion(filter: F, customEventName: String?) {
        val sqlForm = FilterConverter.SQL(filter)
        tracker.converted(
            eventName = customEventName ?: eventName,
            filters = EventObjects.Filters(sqlForm)
        )
    }
    // endregion

    // region Facet tracking methods
    public override fun trackClick(facet: Facet, attribute: Attribute, customEventName: String?) {
        val filterFacet = facet.toFilter(attribute)
        trackClick(filter = filterFacet, customEventName = customEventName)
    }

    public override fun trackView(facet: Facet, attribute: Attribute, customEventName: String?) {
        val filterFacet = facet.toFilter(attribute)
        trackView(filter = filterFacet, customEventName = customEventName)
    }

    public override fun trackConversion(facet: Facet, attribute: Attribute, customEventName: String?) {
        val filterFacet = facet.toFilter(attribute)
        trackConversion(filter = filterFacet, customEventName = customEventName)
    }
    // endregion
}
