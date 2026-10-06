
package com.algolia.instantsearch.insights.internal.data.local.mapper

import com.algolia.client.model.insights.*
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventSubtype
import com.algolia.instantsearch.insights.internal.data.local.model.FilterFacetDO
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.Click
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.Conversion
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.View
import com.algolia.instantsearch.insights.internal.data.local.model.ObjectDataDO
import com.algolia.instantsearch.insights.internal.extension.convertToEventsItem
import com.algolia.instantsearch.filter.Filter

internal object InsightsEventsMapper {

    /**
     * Maps a v3 Insights API event back to its local representation.
     *
     * Every event shape the Insights API accepts is handled, both "after search" (with a `queryID`)
     * and plain (without one). Returns `null` for event shapes this library doesn't track.
     */
    fun eventsItemToDO(input: EventsItems): InsightsEventDO? = when (input) {
        // View
        is EventsItems.ViewedObjectIDsValue -> with(input.value) {
            buildDO(View, eventName, index, userToken, timestamp) { objectIDs = this@with.objectIDs }
        }
        is EventsItems.ViewedFiltersValue -> with(input.value) {
            buildDO(View, eventName, index, userToken, timestamp) { filters = parseFacetFilters(this@with.filters) }
        }
        // Click
        is EventsItems.ClickedObjectIDsAfterSearchValue -> with(input.value) {
            buildDO(Click, eventName, index, userToken, timestamp) {
                objectIDs = this@with.objectIDs
                positions = this@with.positions
                queryID = this@with.queryID
            }
        }
        is EventsItems.ClickedObjectIDsValue -> with(input.value) {
            buildDO(Click, eventName, index, userToken, timestamp) { objectIDs = this@with.objectIDs }
        }
        is EventsItems.ClickedFiltersValue -> with(input.value) {
            buildDO(Click, eventName, index, userToken, timestamp) { filters = parseFacetFilters(this@with.filters) }
        }
        // Conversion
        is EventsItems.ConvertedObjectIDsAfterSearchValue -> with(input.value) {
            buildDO(Conversion, eventName, index, userToken, timestamp) {
                objectIDs = this@with.objectIDs
                queryID = this@with.queryID
            }
        }
        is EventsItems.ConvertedObjectIDsValue -> with(input.value) {
            buildDO(Conversion, eventName, index, userToken, timestamp) { objectIDs = this@with.objectIDs }
        }
        is EventsItems.ConvertedFiltersValue -> with(input.value) {
            buildDO(Conversion, eventName, index, userToken, timestamp) { filters = parseFacetFilters(this@with.filters) }
        }
        // Conversion / purchase
        is EventsItems.PurchasedObjectIDsValue -> with(input.value) {
            buildDO(Conversion, eventName, index, userToken, timestamp) {
                eventSubtype = EventSubtype.Purchase
                objectIDs = this@with.objectIDs
                objectData = this@with.objectData?.map { it.toObjectDataDO() }
                currency = this@with.currency
                value = this@with.value?.toDouble()
            }
        }
        is EventsItems.PurchasedObjectIDsAfterSearchValue -> with(input.value) {
            buildDO(Conversion, eventName, index, userToken, timestamp) {
                eventSubtype = EventSubtype.Purchase
                objectIDs = this@with.objectIDs
                queryID = this@with.objectData.firstNotNullOfOrNull { it.queryID }
                objectData = this@with.objectData.map { it.toObjectDataDO() }
                currency = this@with.currency
                value = this@with.value?.toDouble()
            }
        }
        // Conversion / add to cart
        is EventsItems.AddedToCartObjectIDsValue -> with(input.value) {
            buildDO(Conversion, eventName, index, userToken, timestamp) {
                eventSubtype = EventSubtype.AddToCart
                objectIDs = this@with.objectIDs
                objectData = this@with.objectData?.map { it.toObjectDataDO() }
                currency = this@with.currency
                value = this@with.value?.toDouble()
            }
        }
        is EventsItems.AddedToCartObjectIDsAfterSearchValue -> with(input.value) {
            buildDO(Conversion, eventName, index, userToken, timestamp) {
                eventSubtype = EventSubtype.AddToCart
                objectIDs = this@with.objectIDs
                queryID = this@with.queryID
                objectData = this@with.objectData?.map { it.toObjectDataDO() }
                currency = this@with.currency
                value = this@with.value?.toDouble()
            }
        }
        else -> null
    }

    private inline fun buildDO(
        eventType: InsightsEventDO.EventType,
        eventName: String,
        indexName: String,
        userToken: String?,
        timestamp: Long?,
        block: InsightsEventDO.Builder.() -> Unit,
    ): InsightsEventDO = InsightsEventDO.Builder().apply {
        this.eventType = eventType
        this.eventName = eventName
        this.indexName = indexName
        this.userToken = userToken
        this.timestamp = timestamp
        block()
    }.build()

    private fun parseFacetFilters(filters: List<String>): List<FilterFacetDO> = filters.mapNotNull(::parseFacetFilter)

    fun doToEventsItem(input: InsightsEventDO): EventsItems? {
        return convertToEventsItem(
            eventName = input.eventName,
            indexName = input.indexName,
            eventType = when (input.eventType) {
                View -> "view"
                Click -> "click"
                Conversion -> "conversion"
            },
            eventSubtype = when (input.eventSubtype) {
                EventSubtype.Purchase -> "purchase"
                EventSubtype.AddToCart -> "addToCart"
                null -> null
            },
            userToken = input.userToken,
            timestamp = input.timestamp,
            queryID = input.queryID,
            objectIDs = input.objectIDs,
            positions = input.positions,
            filters = input.filters?.map { FilterFacetMapper.unmap(it) },
            objectData = input.objectData,
            currency = input.currency,
            value = input.value,
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

private fun Price.toDouble(): Double = when (this) {
    is Price.DoubleValue -> value
    is Price.StringValue -> value.toDoubleOrNull() ?: 0.0
}

private fun Discount.toDouble(): Double = when (this) {
    is Discount.DoubleValue -> value
    is Discount.StringValue -> value.toDoubleOrNull() ?: 0.0
}

internal fun Value.toDouble(): Double = when (this) {
    is Value.DoubleValue -> value
    is Value.StringValue -> value.toDoubleOrNull() ?: 0.0
}

internal fun ObjectData.toObjectDataDO(): ObjectDataDO = ObjectDataDO(
    price = price?.toDouble(),
    quantity = quantity,
    discount = discount?.toDouble(),
)

internal fun ObjectDataAfterSearch.toObjectDataDO(): ObjectDataDO = ObjectDataDO(
    queryID = queryID,
    price = price?.toDouble(),
    quantity = quantity,
    discount = discount?.toDouble(),
)
