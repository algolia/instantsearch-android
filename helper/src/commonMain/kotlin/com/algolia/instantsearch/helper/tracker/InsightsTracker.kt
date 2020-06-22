package com.algolia.instantsearch.helper.tracker

import com.algolia.instantsearch.helper.filter.state.toFilter
import com.algolia.instantsearch.helper.searcher.SearcherMultipleIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.insights.FilterTrackable
import com.algolia.instantsearch.insights.HitsAfterSearchTrackable
import com.algolia.instantsearch.insights.Insights
import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterConverter
import com.algolia.search.model.indexing.Indexable
import com.algolia.search.model.search.Facet

public sealed class InsightsTracker<T>(
    public val eventName: String,
    public val searcher: TrackableSearcher,
    public val tracker: T
)

public class HitsTracker(
    eventName: String,
    searcher: TrackableSearcher,
    tracker: HitsAfterSearchTrackable
) : InsightsTracker<HitsAfterSearchTrackable>(eventName, searcher, tracker), QueryIDContainer {

    override var queryID: String? = null

    public constructor(
        eventName: String,
        searcher: SearcherSingleIndex,
        insights: Insights
    ) : this(eventName = eventName, searcher = TrackableSearcher.SingleIndex(searcher), tracker = insights)

    public constructor(
        eventName: String,
        searcher: SearcherMultipleIndex,
        pointer: Int,
        insights: Insights
    ) : this(eventName = eventName, searcher = TrackableSearcher.MultiIndex(searcher, pointer), tracker = insights)

    init {
        searcher.subscribeForQueryIDChange(this) // TODO: connector
        searcher.setClickAnalyticsOn(true)
    }

    // region Hits tracking methods
    public fun <T : Indexable> trackClick(hit: T, position: Int, customEventName: String? = null) {
        val id = queryID ?: return
        tracker.clickedAfterSearch(
            eventName = customEventName ?: eventName,
            queryId = id,
            objectIDs = EventObjects.IDs(hit.objectID.raw),
            positions = listOf(position)
        )
    }

    public fun <T : Indexable> trackConvert(hit: T, customEventName: String? = null) {
        val id = queryID ?: return
        tracker.convertedAfterSearch(
            eventName = customEventName ?: eventName,
            queryId = id,
            objectIDs = EventObjects.IDs(hit.objectID.raw)
        )
    }

    public fun <T : Indexable> trackView(hit: T, customEventName: String? = null) {
        tracker.viewed(
            eventName = customEventName ?: eventName,
            objectIDs = EventObjects.IDs(hit.objectID.raw)
        )
    }
    // endregion
}

public class FilterTracker(
    eventName: String,
    searcher: TrackableSearcher,
    tracker: FilterTrackable
) : InsightsTracker<FilterTrackable>(eventName, searcher, tracker) {

    public constructor(
        eventName: String,
        searcher: SearcherSingleIndex,
        insights: Insights
    ) : this(eventName = eventName, searcher = TrackableSearcher.SingleIndex(searcher), tracker = insights)

    public constructor(
        eventName: String,
        searcher: SearcherMultipleIndex,
        pointer: Int,
        insights: Insights
    ) : this(eventName = eventName, searcher = TrackableSearcher.MultiIndex(searcher, pointer), tracker = insights)

    // region Filter tracking methods
    public fun <F : Filter> trackClick(filter: F, customEventName: String? = null) {
        val sqlForm = FilterConverter.SQL(filter)
        tracker.clicked(
            eventName = customEventName ?: eventName,
            filters = EventObjects.Filters(sqlForm)
        )
    }

    public fun <F : Filter> trackView(filter: F, customEventName: String? = null) {
        val sqlForm = FilterConverter.SQL(filter)
        tracker.viewed(
            eventName = customEventName ?: eventName,
            filters = EventObjects.Filters(sqlForm)
        )
    }

    public fun <F : Filter> trackConversion(filter: F, customEventName: String? = null) {
        val sqlForm = FilterConverter.SQL(filter)
        tracker.converted(
            eventName = customEventName ?: eventName,
            filters = EventObjects.Filters(sqlForm)
        )
    }
    // endregion

    // region Facet tracking methods
    public fun trackClick(facet: Facet, attribute: Attribute, customEventName: String? = null) {
        val filterFacet = facet.toFilter(attribute)
        trackClick(filter = filterFacet, customEventName = customEventName)
    }

    public fun trackView(facet: Facet, attribute: Attribute, customEventName: String? = null) {
        val filterFacet = facet.toFilter(attribute)
        trackView(filter = filterFacet, customEventName = customEventName)
    }

    public fun trackConversion(facet: Facet, attribute: Attribute, customEventName: String? = null) {
        val filterFacet = facet.toFilter(attribute)
        trackConversion(filter = filterFacet, customEventName = customEventName)
    }
    // endregion
}
