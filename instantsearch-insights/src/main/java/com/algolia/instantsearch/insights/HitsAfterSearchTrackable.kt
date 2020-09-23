package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.instantsearch.insights.internal.extension.currentTimeMillis

public interface HitsAfterSearchTrackable {

    /**
     * Tracks a View event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param objectIDs the viewed object(s)' `objectID`.
     * @param timestamp the time at which the view happened. Defaults to current time.
     */
    public fun viewed(
        eventName: String,
        objectIDs: EventObjects.IDs,
        timestamp: Long = currentTimeMillis,
    )

    /**
     * Tracks a click event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param objectIDs the clicked object(s)' `objectID`.
     * @param timestamp the time at which the click happened. Defaults to current time.
     */
    public fun clicked(
        eventName: String,
        objectIDs: EventObjects.IDs,
        timestamp: Long = currentTimeMillis,
    )

    /**
     * Tracks a Conversion event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param objectIDs the object(s)' `objectID`.
     * @param timestamp the time at which the conversion happened. Defaults to current time.
     */
    public fun converted(
        eventName: String,
        objectIDs: EventObjects.IDs,
        timestamp: Long = currentTimeMillis,
    )

    /**
     * Tracks a Click event after a search has been done.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param queryId the related [query's identifier][https://www.algolia.com/doc/guides/insights-and-analytics/click-analytics/?language=php#identifying-the-query-result-position].
     * @param objectIDs the object(s)' `objectID`.
     * @param positions the clicked object(s)' position(s).
     * @param timestamp the time at which the click happened. Defaults to current time.
     */
    public fun clickedAfterSearch(
        eventName: String,
        queryId: String,
        objectIDs: EventObjects.IDs,
        positions: List<Int>,
        timestamp: Long = currentTimeMillis,
    )

    /**
     * Tracks a Conversion event after a search has been done.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param queryId the related [query's identifier][https://www.algolia.com/doc/guides/insights-and-analytics/click-analytics/?language=php#identifying-the-query-result-position].
     * @param objectIDs the object(s)' `objectID`.
     * @param timestamp the time at which the conversion happened. Defaults to current time.
     */
    public fun convertedAfterSearch(
        eventName: String,
        queryId: String,
        objectIDs: EventObjects.IDs,
        timestamp: Long = currentTimeMillis,
    )
}
