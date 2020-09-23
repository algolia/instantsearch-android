package com.algolia.instantsearch.insights.internal.converter

import com.algolia.instantsearch.insights.event.EventKey
import com.algolia.instantsearch.insights.event.EventType
import com.algolia.instantsearch.insights.internal.event.EventInternal
import com.algolia.instantsearch.insights.internal.extension.toList
import com.algolia.search.model.IndexName
import com.algolia.search.model.insights.InsightsEvent
import com.algolia.search.model.insights.InsightsEvent.Click
import com.algolia.search.model.insights.InsightsEvent.Conversion
import com.algolia.search.model.insights.InsightsEvent.View
import org.json.JSONArray
import org.json.JSONObject

internal object ConverterEventToEventInternal : Converter<Pair<InsightsEvent, IndexName>, EventInternal> {

    override fun convert(input: Pair<InsightsEvent, IndexName>): EventInternal {
        val (event, indexName) = input

        return when (event) {
            is View -> event.toEventInternal(indexName)
            is Conversion -> event.toEventInternal(indexName)
            is Click -> event.toEventInternal(indexName)
        }
    }

    private fun Click.toEventInternal(indexName: IndexName): EventInternal {
        return listOfNotNull(
            EventKey.EventType.key to EventType.Click.key,
            EventKey.EventName.key to eventName.raw,
            EventKey.IndexName.key to indexName.raw,
            EventKey.Timestamp.key to timestamp,
            EventKey.QueryId.key to queryID?.raw,
            EventKey.UserToken.key to userToken?.raw,
            EventKey.Positions.key to positions,
            objectIDsOrNull(resources),
            filtersOrNull(resources)
        ).toMap()
    }

    private fun Conversion.toEventInternal(indexName: IndexName): EventInternal {
        return listOfNotNull(
            EventKey.EventType.key to EventType.Conversion.key,
            EventKey.EventName.key to eventName.raw,
            EventKey.IndexName.key to indexName.raw,
            EventKey.Timestamp.key to timestamp,
            EventKey.QueryId.key to queryID?.raw,
            EventKey.UserToken.key to userToken?.raw,
            objectIDsOrNull(resources),
            filtersOrNull(resources)
        ).toMap()
    }

    private fun View.toEventInternal(indexName: IndexName): EventInternal {
        return listOfNotNull(
            EventKey.EventType.key to EventType.View.key,
            EventKey.EventName.key to eventName.raw,
            EventKey.IndexName.key to indexName.raw,
            EventKey.Timestamp.key to timestamp,
            EventKey.QueryId.key to queryID?.raw,
            EventKey.UserToken.key to userToken?.raw,
            objectIDsOrNull(resources),
            filtersOrNull(resources)
        ).toMap()
    }

    private fun objectIDsOrNull(resources: InsightsEvent.Resources?) =
        if (resources is InsightsEvent.Resources.ObjectIDs) EventKey.ObjectIds.key to resources.objectIDs.map { it.raw } else null

    private fun filtersOrNull(resources: InsightsEvent.Resources?) =
        if (resources is InsightsEvent.Resources.Filters) EventKey.Filters.key to resources.filters.map { "${it.attribute}:${it.value}" } else null
}

internal object ConverterStringToEventInternal : Converter<String, EventInternal> {

    override fun convert(input: String): EventInternal {
        val json = JSONObject(input)
        return json.keys()
            .asSequence()
            .map { it to if (json.get(it) is JSONArray) json.getJSONArray(it).toList() else json.get(it) }
            .toMap()
    }
}
