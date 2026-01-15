package com.algolia.instantsearch.insights

public interface HitsAfterSearchTrackable {

    /**
     * Tracks a View event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param objectIDs the viewed object(s)' `objectID`.
     * @param timestamp the time at which the view happened. Defaults to current time.
     */
    public fun viewedObjectIDs(
        eventName: String,
        objectIDs: List<String>,
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
        eventName: String,
        objectIDs: List<String>,
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
        eventName: String,
        objectIDs: List<String>,
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
        eventName: String,
        queryID: String,
        objectIDs: List<String>,
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
        eventName: String,
        queryID: String,
        objectIDs: List<String>,
        timestamp: Long? = null,
    )
}
