package com.algolia.instantsearch.insights.internal.extension

import com.algolia.client.model.insights.*
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.filter.FilterConverter
import com.algolia.instantsearch.insights.internal.data.local.model.ObjectDataDO

/**
 * Converts Filter.Facet to v3 filter string format (facet:value)
 */
internal fun Filter.Facet.toFilterString(): String {
    val filterStrings = FilterConverter.Legacy(this)
    return filterStrings.firstOrNull() ?: ""
}

/**
 * Converts a list of Filter.Facet to v3 filter strings
 */
internal fun List<Filter.Facet>.toFilterStrings(): List<String> {
    return flatMap { FilterConverter.Legacy(it) }
}

/**
 * Converts InsightsEventDO-like data to v3 EventsItems
 */
internal fun convertToEventsItem(
    eventName: String,
    indexName: String,
    eventType: String, // "view", "click", "conversion"
    eventSubtype: String?, // "purchase", "addToCart"
    userToken: String?,
    timestamp: Long?,
    queryID: String?,
    objectIDs: List<String>?,
    positions: List<Int>?,
    filters: List<Filter.Facet>?,
    objectData: List<ObjectDataDO>?,
    currency: String?,
    value: Double?,
): EventsItems? {
    val userTokenValue = userToken ?: return null
    val valueWrapped = value?.let { Value.of(it) }

    return when (eventType) {
        "view" -> convertViewEvent(eventName, indexName, userTokenValue, timestamp, objectIDs, filters)
        "click" -> convertClickEvent(eventName, indexName, userTokenValue, timestamp, queryID, objectIDs, positions, filters)
        "conversion" -> convertConversionEvent(
            eventName, indexName, eventSubtype, userTokenValue, timestamp,
            queryID, objectIDs, filters, objectData, currency, valueWrapped
        )
        else -> null
    }
}

private fun convertViewEvent(
    eventName: String,
    indexName: String,
    userToken: String,
    timestamp: Long?,
    objectIDs: List<String>?,
    filters: List<Filter.Facet>?,
): EventsItems? = when {
    objectIDs != null -> EventsItems.of(ViewedObjectIDs(
        eventName = eventName,
        eventType = ViewEvent.View,
        index = indexName,
        objectIDs = objectIDs,
        userToken = userToken,
        timestamp = timestamp,
    ))
    filters != null -> EventsItems.of(ViewedFilters(
        eventName = eventName,
        eventType = ViewEvent.View,
        index = indexName,
        filters = filters.toFilterStrings(),
        userToken = userToken,
        timestamp = timestamp,
    ))
    else -> null
}

private fun convertClickEvent(
    eventName: String,
    indexName: String,
    userToken: String,
    timestamp: Long?,
    queryID: String?,
    objectIDs: List<String>?,
    positions: List<Int>?,
    filters: List<Filter.Facet>?,
): EventsItems? = when {
    queryID != null && objectIDs != null && positions != null -> EventsItems.of(ClickedObjectIDsAfterSearch(
        eventName = eventName,
        eventType = ClickEvent.Click,
        index = indexName,
        objectIDs = objectIDs,
        positions = positions,
        queryID = queryID,
        userToken = userToken,
        timestamp = timestamp,
    ))
    filters != null -> EventsItems.of(ClickedFilters(
        eventName = eventName,
        eventType = ClickEvent.Click,
        index = indexName,
        filters = filters.toFilterStrings(),
        userToken = userToken,
        timestamp = timestamp,
    ))
    else -> null
}

private fun convertConversionEvent(
    eventName: String,
    indexName: String,
    eventSubtype: String?,
    userToken: String,
    timestamp: Long?,
    queryID: String?,
    objectIDs: List<String>?,
    filters: List<Filter.Facet>?,
    objectData: List<ObjectDataDO>?,
    currency: String?,
    value: Value?,
): EventsItems? = when (eventSubtype) {
    "purchase" -> convertPurchaseEvent(eventName, indexName, userToken, timestamp, queryID, objectIDs, objectData, currency, value)
    "addToCart" -> convertAddToCartEvent(eventName, indexName, userToken, timestamp, queryID, objectIDs, objectData, currency, value)
    else -> convertPlainConversionEvent(eventName, indexName, userToken, timestamp, queryID, objectIDs, filters)
}

private fun convertPlainConversionEvent(
    eventName: String,
    indexName: String,
    userToken: String,
    timestamp: Long?,
    queryID: String?,
    objectIDs: List<String>?,
    filters: List<Filter.Facet>?,
): EventsItems? = when {
    queryID != null && objectIDs != null -> EventsItems.of(ConvertedObjectIDsAfterSearch(
        eventName = eventName,
        eventType = ConversionEvent.Conversion,
        index = indexName,
        objectIDs = objectIDs,
        queryID = queryID,
        userToken = userToken,
        timestamp = timestamp,
    ))
    filters != null -> EventsItems.of(ConvertedFilters(
        eventName = eventName,
        eventType = ConversionEvent.Conversion,
        index = indexName,
        filters = filters.toFilterStrings(),
        userToken = userToken,
        timestamp = timestamp,
    ))
    else -> null
}

private fun convertPurchaseEvent(
    eventName: String,
    indexName: String,
    userToken: String,
    timestamp: Long?,
    queryID: String?,
    objectIDs: List<String>?,
    objectData: List<ObjectDataDO>?,
    currency: String?,
    value: Value?,
): EventsItems? {
    if (objectIDs == null) return null
    return if (queryID != null) {
        val mappedObjectData = objectData?.map { it.toObjectDataAfterSearch() }
            ?: objectIDs.map { ObjectDataAfterSearch(queryID = queryID) }
        EventsItems.of(PurchasedObjectIDsAfterSearch(
            eventName = eventName,
            eventType = ConversionEvent.Conversion,
            eventSubtype = PurchaseEvent.Purchase,
            index = indexName,
            objectIDs = objectIDs,
            userToken = userToken,
            objectData = mappedObjectData,
            timestamp = timestamp,
            currency = currency,
            value = value,
        ))
    } else {
        EventsItems.of(PurchasedObjectIDs(
            eventName = eventName,
            eventType = ConversionEvent.Conversion,
            eventSubtype = PurchaseEvent.Purchase,
            index = indexName,
            objectIDs = objectIDs,
            userToken = userToken,
            objectData = objectData?.map { it.toObjectData() },
            timestamp = timestamp,
            currency = currency,
            value = value,
        ))
    }
}

private fun convertAddToCartEvent(
    eventName: String,
    indexName: String,
    userToken: String,
    timestamp: Long?,
    queryID: String?,
    objectIDs: List<String>?,
    objectData: List<ObjectDataDO>?,
    currency: String?,
    value: Value?,
): EventsItems? {
    if (objectIDs == null) return null
    return if (queryID != null) {
        EventsItems.of(AddedToCartObjectIDsAfterSearch(
            eventName = eventName,
            eventType = ConversionEvent.Conversion,
            eventSubtype = AddToCartEvent.AddToCart,
            index = indexName,
            objectIDs = objectIDs,
            queryID = queryID,
            userToken = userToken,
            objectData = objectData?.map { it.toObjectDataAfterSearch() },
            timestamp = timestamp,
            currency = currency,
            value = value,
        ))
    } else {
        EventsItems.of(AddedToCartObjectIDs(
            eventName = eventName,
            eventType = ConversionEvent.Conversion,
            eventSubtype = AddToCartEvent.AddToCart,
            index = indexName,
            objectIDs = objectIDs,
            userToken = userToken,
            objectData = objectData?.map { it.toObjectData() },
            timestamp = timestamp,
            currency = currency,
            value = value,
        ))
    }
}

internal fun ObjectDataDO.toObjectData(): ObjectData = ObjectData(
    price = price?.let { Price.of(it) },
    quantity = quantity,
    discount = discount?.let { Discount.of(it) },
)

internal fun ObjectDataDO.toObjectDataAfterSearch(): ObjectDataAfterSearch = ObjectDataAfterSearch(
    queryID = queryID,
    price = price?.let { Price.of(it) },
    quantity = quantity,
    discount = discount?.let { Discount.of(it) },
)
