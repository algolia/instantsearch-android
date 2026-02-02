package com.algolia.instantsearch.insights.internal.extension

import com.algolia.client.model.insights.*
import com.algolia.instantsearch.filter.Filter
import com.algolia.instantsearch.filter.FilterConverter

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
    userToken: String?,
    timestamp: Long?,
    queryID: String?,
    objectIDs: List<String>?,
    positions: List<Int>?,
    filters: List<Filter.Facet>?,
): EventsItems? {
    val userTokenValue = userToken ?: return null

    return when (eventType) {
        "view" -> {
            when {
                objectIDs != null -> {
                    ViewedObjectIDs(
                        eventName = eventName,
                        eventType = ViewEvent.View,
                        index = indexName,
                        objectIDs = objectIDs,
                        userToken = userTokenValue,
                        timestamp = timestamp
                    )
                }
                filters != null -> {
                    ViewedFilters(
                        eventName = eventName,
                        eventType = ViewEvent.View,
                        index = indexName,
                        filters = filters.toFilterStrings(),
                        userToken = userTokenValue,
                        timestamp = timestamp
                    )
                }
                else -> null
            }
        }
        "click" -> {
            when {
                queryID != null && objectIDs != null && positions != null -> {
                    ClickedObjectIDsAfterSearch(
                        eventName = eventName,
                        eventType = ClickEvent.Click,
                        index = indexName,
                        objectIDs = objectIDs,
                        positions = positions,
                        queryID = queryID,
                        userToken = userTokenValue,
                        timestamp = timestamp
                    )
                }
                filters != null -> {
                    ClickedFilters(
                        eventName = eventName,
                        eventType = ClickEvent.Click,
                        index = indexName,
                        filters = filters.toFilterStrings(),
                        userToken = userTokenValue,
                        timestamp = timestamp
                    )
                }
                else -> null
            }
        }
        "conversion" -> {
            when {
                queryID != null && objectIDs != null -> {
                    ConvertedObjectIDsAfterSearch(
                        eventName = eventName,
                        eventType = ConversionEvent.Conversion,
                        index = indexName,
                        objectIDs = objectIDs,
                        queryID = queryID,
                        userToken = userTokenValue,
                        timestamp = timestamp
                    )
                }
                filters != null -> {
                    ConvertedFilters(
                        eventName = eventName,
                        eventType = ConversionEvent.Conversion,
                        index = indexName,
                        filters = filters.toFilterStrings(),
                        userToken = userTokenValue,
                        timestamp = timestamp
                    )
                }
                else -> null
            }
        }
        else -> null
    }
}
