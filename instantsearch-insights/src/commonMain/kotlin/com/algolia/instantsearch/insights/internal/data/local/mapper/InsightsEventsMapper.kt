
package com.algolia.instantsearch.insights.internal.data.local.mapper

import com.algolia.client.model.insights.*
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.Click
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.Conversion
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.View
import com.algolia.instantsearch.insights.internal.extension.convertToEventsItem
import com.algolia.instantsearch.filter.Filter

internal object InsightsEventsMapper {

    fun eventsItemToDO(input: EventsItems): com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO {
        val builder = InsightsEventDO.Builder()
        when (input) {
            is EventsItems.ViewedObjectIDsValue -> {
                builder.eventType = View
                builder.objectIDs = input.value.objectIDs
            }
            is EventsItems.ViewedFiltersValue -> {
                builder.eventType = View
                builder.filters = input.value.filters.mapNotNull { filterString -> 
                    // Convert filter string back to Filter.Facet for storage
                    // This is a simplification - in reality you'd parse the string
                    null // TODO: implement filter string parsing if needed
                }
            }
            is EventsItems.ClickedObjectIDsAfterSearchValue -> {
                builder.eventType = Click
                builder.objectIDs = input.value.objectIDs
                builder.positions = input.value.positions
                builder.queryID = input.value.queryID
            }
            is EventsItems.ClickedFiltersValue -> {
                builder.eventType = Click
                builder.filters = input.value.filters.mapNotNull { null } // TODO: parse filter strings
            }
            is EventsItems.ConvertedObjectIDsAfterSearchValue -> {
                builder.eventType = Conversion
                builder.objectIDs = input.value.objectIDs
                builder.queryID = input.value.queryID
            }
            is EventsItems.ConvertedFiltersValue -> {
                builder.eventType = Conversion
                builder.filters = input.value.filters.mapNotNull { null } // TODO: parse filter strings
            }
            else -> return builder.build() // Other event types not yet handled
        }
        builder.eventName = when (input) {
            is EventsItems.ViewedObjectIDsValue -> input.value.eventName
            is EventsItems.ViewedFiltersValue -> input.value.eventName
            is EventsItems.ClickedObjectIDsAfterSearchValue -> input.value.eventName
            is EventsItems.ClickedFiltersValue -> input.value.eventName
            is EventsItems.ConvertedObjectIDsAfterSearchValue -> input.value.eventName
            is EventsItems.ConvertedFiltersValue -> input.value.eventName
            else -> ""
        }
        builder.indexName = when (input) {
            is EventsItems.ViewedObjectIDsValue -> input.value.index
            is EventsItems.ViewedFiltersValue -> input.value.index
            is EventsItems.ClickedObjectIDsAfterSearchValue -> input.value.index
            is EventsItems.ClickedFiltersValue -> input.value.index
            is EventsItems.ConvertedObjectIDsAfterSearchValue -> input.value.index
            is EventsItems.ConvertedFiltersValue -> input.value.index
            else -> ""
        }
        builder.userToken = when (input) {
            is EventsItems.ViewedObjectIDsValue -> input.value.userToken
            is EventsItems.ViewedFiltersValue -> input.value.userToken
            is EventsItems.ClickedObjectIDsAfterSearchValue -> input.value.userToken
            is EventsItems.ClickedFiltersValue -> input.value.userToken
            is EventsItems.ConvertedObjectIDsAfterSearchValue -> input.value.userToken
            is EventsItems.ConvertedFiltersValue -> input.value.userToken
            else -> null
        }
        builder.timestamp = when (input) {
            is EventsItems.ViewedObjectIDsValue -> input.value.timestamp
            is EventsItems.ViewedFiltersValue -> input.value.timestamp
            is EventsItems.ClickedObjectIDsAfterSearchValue -> input.value.timestamp
            is EventsItems.ClickedFiltersValue -> input.value.timestamp
            is EventsItems.ConvertedObjectIDsAfterSearchValue -> input.value.timestamp
            is EventsItems.ConvertedFiltersValue -> input.value.timestamp
            else -> null
        }
        return builder.build()
    }

    fun doToEventsItem(input: com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO): EventsItems? {
        return convertToEventsItem(
            eventName = input.eventName,
            indexName = input.indexName,
            eventType = when (input.eventType) {
                View -> "view"
                Click -> "click"
                Conversion -> "conversion"
            },
            userToken = input.userToken,
            timestamp = input.timestamp,
            queryID = input.queryID,
            objectIDs = input.objectIDs,
            positions = input.positions,
            filters = input.filters?.map { FilterFacetMapper.unmap(it) }
        )
    }
}
