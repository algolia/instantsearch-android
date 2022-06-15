package com.algolia.instantsearch.insights

import com.algolia.search.model.ObjectID
import com.algolia.search.model.QueryID
import com.algolia.search.model.insights.EventName

public interface HitsAfterSearchTrackable {

    /**
     * Tracks a View event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param objectIDs the viewed object(s)' `objectID`.
     * @param timestamp the time at which the view happened. Defaults to current time.
     */
    public fun viewedObjectIDs(
        eventName: EventName,
        objectIDs: List<ObjectID>,
        timestamp: Long? = null,
    )

    /**
     * Tracks a click event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param objectIDs the clicked object(s)' `objectID`.
     * @param timestamp the time at which the click happened.
     */
    public fun clickedObjectIDs(
        eventName: EventName,
        objectIDs: List<ObjectID>,
        timestamp: Long? = null,
    )

    /**
     * Tracks a Conversion event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param objectIDs the object(s)' `objectID`.
     * @param timestamp the time at which the conversion happened.
     */
    public fun convertedObjectIDs(
        eventName: EventName,
        objectIDs: List<ObjectID>,
        timestamp: Long? = null,
    )

    /**
     * Tracks a Click event after a search has been done.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param queryID the related [query's identifier][https://www.algolia.com/doc/guides/insights-and-analytics/click-analytics/?language=php#identifying-the-query-result-position].
     * @param objectIDs the object(s)' `objectID`.
     * @param positions the clicked object(s)' position(s).
     * @param timestamp the time at which the click happened.
     */
    public fun clickedObjectIDsAfterSearch(
        eventName: EventName,
        queryID: QueryID,
        objectIDs: List<ObjectID>,
        positions: List<Int>,
        timestamp: Long? = null,
    )

    /**
     * Tracks a Conversion event after a search has been done.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param queryID the related [query's identifier][https://www.algolia.com/doc/guides/insights-and-analytics/click-analytics/?language=php#identifying-the-query-result-position].
     * @param objectIDs the object(s)' `objectID`.
     * @param timestamp the time at which the conversion happened.
     */
    public fun convertedObjectIDsAfterSearch(
        eventName: EventName,
        queryID: QueryID,
        objectIDs: List<ObjectID>,
        timestamp: Long? = null,
    )
}
