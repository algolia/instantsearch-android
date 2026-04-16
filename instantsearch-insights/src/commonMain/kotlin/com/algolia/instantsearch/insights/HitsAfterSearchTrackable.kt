package com.algolia.instantsearch.insights

import com.algolia.client.model.insights.ObjectData
import com.algolia.client.model.insights.ObjectDataAfterSearch
import com.algolia.client.model.insights.Value

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

    /**
     * Tracks a Purchase event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param objectIDs the purchased object(s)' `objectID`.
     * @param objectData extra information about the purchased items (price, quantity, discount).
     * @param currency three-letter [ISO 4217](https://www.iso.org/iso-4217-currency-codes.html) currency code.
     * @param value total monetary value of this event in units of [currency].
     * @param timestamp the time at which the purchase happened.
     */
    public fun purchasedObjectIDs(
        eventName: String,
        objectIDs: List<String>,
        objectData: List<ObjectData>? = null,
        currency: String? = null,
        value: Value? = null,
        timestamp: Long? = null,
    )

    /**
     * Tracks a Purchase event after a search has been done.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param queryID the related query's identifier.
     * @param objectIDs the purchased object(s)' `objectID`.
     * @param objectData extra information about the purchased items (price, quantity, discount, per-item queryID).
     * @param currency three-letter [ISO 4217](https://www.iso.org/iso-4217-currency-codes.html) currency code.
     * @param value total monetary value of this event in units of [currency].
     * @param timestamp the time at which the purchase happened.
     */
    public fun purchasedObjectIDsAfterSearch(
        eventName: String,
        queryID: String,
        objectIDs: List<String>,
        objectData: List<ObjectDataAfterSearch>? = null,
        currency: String? = null,
        value: Value? = null,
        timestamp: Long? = null,
    )

    /**
     * Tracks an Add to Cart event, unrelated to a specific search query.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param objectIDs the added object(s)' `objectID`.
     * @param objectData extra information about the added items (price, quantity, discount).
     * @param currency three-letter [ISO 4217](https://www.iso.org/iso-4217-currency-codes.html) currency code.
     * @param value total monetary value of this event in units of [currency].
     * @param timestamp the time at which the add-to-cart happened.
     */
    public fun addedToCartObjectIDs(
        eventName: String,
        objectIDs: List<String>,
        objectData: List<ObjectData>? = null,
        currency: String? = null,
        value: Value? = null,
        timestamp: Long? = null,
    )

    /**
     * Tracks an Add to Cart event after a search has been done.
     *
     * @param eventName the event's name, **must not be empty**.
     * @param queryID the related query's identifier.
     * @param objectIDs the added object(s)' `objectID`.
     * @param objectData extra information about the added items (price, quantity, discount, per-item queryID).
     * @param currency three-letter [ISO 4217](https://www.iso.org/iso-4217-currency-codes.html) currency code.
     * @param value total monetary value of this event in units of [currency].
     * @param timestamp the time at which the add-to-cart happened.
     */
    public fun addedToCartObjectIDsAfterSearch(
        eventName: String,
        queryID: String,
        objectIDs: List<String>,
        objectData: List<ObjectDataAfterSearch>? = null,
        currency: String? = null,
        value: Value? = null,
        timestamp: Long? = null,
    )
}
