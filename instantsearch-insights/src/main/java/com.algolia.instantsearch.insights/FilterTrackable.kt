package com.algolia.instantsearch.insights

import com.algolia.instantsearch.insights.event.EventObjects
import com.algolia.instantsearch.insights.internal.extension.currentTimeMillis

public interface FilterTrackable {

    /**
     * Tracks a View event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param filters the clicked filter(s).
     * @param timestamp the time at which the view happened. Defaults to current time.
     */
    public fun viewed(
        eventName: String,
        filters: EventObjects.Filters,
        timestamp: Long = currentTimeMillis
    )

    /**
     * Tracks a click event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param filters the clicked filter(s).
     * @param timestamp the time at which the click happened. Defaults to current time.
     */
    public fun clicked(
        eventName: String,
        filters: EventObjects.Filters,
        timestamp: Long = currentTimeMillis
    )

    /**
     * Tracks a Conversion event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param timestamp the time at which the conversion happened. Defaults to current time.
     * @param filters the converted filter(s).
     */
    public fun converted(
        eventName: String,
        filters: EventObjects.Filters,
        timestamp: Long = currentTimeMillis
    )
}
