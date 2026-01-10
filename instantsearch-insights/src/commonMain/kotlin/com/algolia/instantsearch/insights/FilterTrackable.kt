package com.algolia.instantsearch.insights

import com.algolia.instantsearch.migration2to3.Filter
import com.algolia.instantsearch.migration2to3.EventName

public interface FilterTrackable {

    /**
     * Tracks a View event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param filters the clicked filter(s).
     * @param timestamp the time at which the view happened.
     */
    public fun viewedFilters(
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long? = null,
    )

    /**
     * Tracks a click event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param filters the clicked filter(s).
     * @param timestamp the time at which the click happened.
     */
    public fun clickedFilters(
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long? = null,
    )

    /**
     * Tracks a Conversion event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param timestamp the time at which the conversion happened.
     * @param filters the converted filter(s).
     */
    public fun convertedFilters(
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long? = null,
    )
}
