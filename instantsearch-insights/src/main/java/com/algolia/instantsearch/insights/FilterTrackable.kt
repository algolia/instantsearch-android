package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.internal.extension.currentTimeMillis
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.EventName

public interface FilterTrackable {

    /**
     * Tracks a View event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param filters the clicked filter(s).
     * @param timestamp the time at which the view happened. Defaults to current time.
     */
    public fun viewedFilters(
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long = currentTimeMillis,
    )

    /**
     * Tracks a click event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param filters the clicked filter(s).
     * @param timestamp the time at which the click happened. Defaults to current time.
     */
    public fun clickedFilters(
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long = currentTimeMillis,
    )

    /**
     * Tracks a Conversion event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param timestamp the time at which the conversion happened. Defaults to current time.
     * @param filters the converted filter(s).
     */
    public fun convertedFilters(
        eventName: EventName,
        filters: List<Filter.Facet>,
        timestamp: Long = currentTimeMillis,
    )
}
