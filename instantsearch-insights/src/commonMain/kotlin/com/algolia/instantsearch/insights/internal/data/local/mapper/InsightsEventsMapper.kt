
package com.algolia.instantsearch.insights.internal.data.local.mapper

import com.algolia.client.model.insights.*
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.data.local.model.FilterFacetDO
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
                builder.filters = input.value.filters.mapNotNull(::parseFacetFilter)
            }
            is EventsItems.ClickedObjectIDsAfterSearchValue -> {
                builder.eventType = Click
                builder.objectIDs = input.value.objectIDs
                builder.positions = input.value.positions
                builder.queryID = input.value.queryID
            }
            is EventsItems.ClickedFiltersValue -> {
                builder.eventType = Click
                builder.filters = input.value.filters.mapNotNull(::parseFacetFilter)
            }
            is EventsItems.ConvertedObjectIDsAfterSearchValue -> {
                builder.eventType = Conversion
                builder.objectIDs = input.value.objectIDs
                builder.queryID = input.value.queryID
            }
            is EventsItems.ConvertedFiltersValue -> {
                builder.eventType = Conversion
                builder.filters = input.value.filters.mapNotNull(::parseFacetFilter)
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

    private fun parseFacetFilter(filterString: String): FilterFacetDO? {
        val trimmed = filterString.trim()
        val (rawAttribute, rawValue) = splitOnFirstColonOutsideQuotes(trimmed) ?: return null
        val attribute = rawAttribute.trim().unquote()
        if (attribute.isEmpty()) return null

        var valuePart = rawValue.trim()
        val scoreMatch = SCORE_REGEX.find(valuePart)
        val score = scoreMatch?.groupValues?.getOrNull(1)?.toIntOrNull()
        if (scoreMatch != null) {
            valuePart = valuePart.removeRange(scoreMatch.range).trim()
        }
        val isNegated = valuePart.startsWith("-") && !valuePart.startsWith("-\"")
        if (isNegated) {
            valuePart = valuePart.removePrefix("-").trim()
        }
        val value = parseFacetValue(valuePart)
        val facet = Filter.Facet(attribute = attribute, isNegated = isNegated, value = value, score = score)
        return FilterFacetMapper.map(facet)
    }

    private fun splitOnFirstColonOutsideQuotes(input: String): Pair<String, String>? {
        var inQuotes = false
        input.forEachIndexed { index, char ->
            if (char == '"' && (index == 0 || input[index - 1] != '\\')) {
                inQuotes = !inQuotes
            } else if (char == ':' && !inQuotes) {
                val left = input.substring(0, index)
                val right = input.substring(index + 1)
                return left to right
            }
        }
        return null
    }

    private fun String.unquote(): String {
        val trimmed = trim()
        if (trimmed.length >= 2 && trimmed.first() == '"' && trimmed.last() == '"') {
            return trimmed.substring(1, trimmed.length - 1)
                .replace("\\\"", "\"")
                .replace("\\\\", "\\")
        }
        return trimmed
    }

    private fun parseFacetValue(valuePart: String): Filter.Facet.Value {
        val raw = valuePart.unquote()
        raw.toLongOrNull()?.let { return Filter.Facet.Value.Number(it) }
        raw.toDoubleOrNull()?.let { return Filter.Facet.Value.Number(it) }
        raw.toBooleanStrictOrNull()?.let { return Filter.Facet.Value.Boolean(it) }
        return Filter.Facet.Value.String(raw)
    }

    private val SCORE_REGEX = Regex("<score=(\\d+)>\\s*$")
}
