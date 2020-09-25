package com.algolia.instantsearch.insights.internal.database.mapper

import com.algolia.instantsearch.insights.internal.database.model.EventKey
import com.algolia.instantsearch.insights.internal.database.model.EventType
import com.algolia.instantsearch.insights.internal.database.model.FilterFacetDO
import com.algolia.instantsearch.insights.internal.database.model.FacetKey
import com.algolia.instantsearch.insights.internal.database.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.database.model.ValueType
import com.algolia.search.helper.toAttribute
import com.algolia.search.helper.toEventName
import com.algolia.search.helper.toIndexName
import com.algolia.search.helper.toObjectID
import com.algolia.search.helper.toQueryID
import com.algolia.search.helper.toUserToken
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.insights.InsightsEvent

internal object InsightsEventsMapper : Mapper<InsightsEvent, InsightsEventDO> {

    override fun map(input: InsightsEvent): InsightsEventDO {
        return when (input) {
            is InsightsEvent.View -> input.toEventDO()
            is InsightsEvent.Conversion -> input.toEventDO()
            is InsightsEvent.Click -> input.toEventDO()
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun InsightsEvent.Click.toEventDO(): InsightsEventDO {
        return buildMap {
            put(EventKey.EventType.raw, EventType.Click.raw)
            put(EventKey.EventName.raw, eventName.raw)
            put(EventKey.IndexName.raw, indexName.raw)
            put(EventKey.Timestamp.raw, timestamp)
            queryID?.let { put(EventKey.QueryID.raw, it.raw) }
            userToken?.let { put(EventKey.UserToken.raw, it.raw) }
            put(EventKey.Positions.raw, positions)
            objectIDsOrNull(resources)?.let { put(EventKey.ObjectIds.raw, it) }
            filtersOrNull(resources)?.let { put(EventKey.Filters.raw, it) }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun InsightsEvent.Conversion.toEventDO(): InsightsEventDO {
        return buildMap {
            put(EventKey.EventType.raw, EventType.Conversion.raw)
            put(EventKey.EventName.raw, eventName.raw)
            put(EventKey.IndexName.raw, indexName.raw)
            put(EventKey.Timestamp.raw, timestamp)
            queryID?.let { put(EventKey.QueryID.raw, it.raw) }
            userToken?.let { put(EventKey.UserToken.raw, it.raw) }
            objectIDsOrNull(resources)?.let { put(EventKey.ObjectIds.raw, it) }
            filtersOrNull(resources)?.let { put(EventKey.Filters.raw, it) }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun InsightsEvent.View.toEventDO(): InsightsEventDO {
        return buildMap {
            put(EventKey.EventType.raw, EventType.View.raw)
            put(EventKey.EventName.raw, eventName.raw)
            put(EventKey.IndexName.raw, indexName.raw)
            put(EventKey.Timestamp.raw, timestamp)
            queryID?.let { put(EventKey.QueryID.raw, it.raw) }
            userToken?.let { put(EventKey.UserToken.raw, it.raw) }
            objectIDsOrNull(resources)?.let { put(EventKey.ObjectIds.raw, it) }
            filtersOrNull(resources)?.let { put(EventKey.Filters.raw, it) }
        }
    }

    private fun objectIDsOrNull(resources: InsightsEvent.Resources?): List<String>? {
        return if (resources is InsightsEvent.Resources.ObjectIDs) resources.objectIDs.map { it.raw } else null
    }

    private fun filtersOrNull(resources: InsightsEvent.Resources?): List<FilterFacetDO>? {
        return if (resources is InsightsEvent.Resources.Filters) resources.filters.map { it.asMap() } else null
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun Filter.Facet.asMap(): FilterFacetDO {
        return buildMap {
            put(FacetKey.Attribute.raw, attribute.raw)
            put(FacetKey.IsNegated.raw, isNegated)
            put(FacetKey.Value.raw, value.getRaw())
            put(FacetKey.ValueType.raw, value.getType().raw)
            score?.let { put(FacetKey.Score.raw, it) }
        }
    }

    private fun Filter.Facet.Value.getRaw(): Any {
        return when (this) {
            is Filter.Facet.Value.String -> raw
            is Filter.Facet.Value.Boolean -> raw
            is Filter.Facet.Value.Number -> raw
        }
    }

    private fun Filter.Facet.Value.getType(): ValueType {
        return when (this) {
            is Filter.Facet.Value.String -> ValueType.String
            is Filter.Facet.Value.Boolean -> ValueType.Boolean
            is Filter.Facet.Value.Number -> ValueType.Number
        }
    }

    override fun unmap(input: InsightsEventDO): InsightsEvent {
        return when (input[EventKey.EventType.raw]) {
            EventType.View.raw -> input.toViewEvent()
            EventType.Conversion.raw -> input.toConversionEvent()
            EventType.Click.raw -> input.toClickEvent()
            else -> throw UnsupportedOperationException()
        }
    }

    private fun InsightsEventDO.toClickEvent(): InsightsEvent.Click {
        @Suppress("UNCHECKED_CAST")
        return InsightsEvent.Click(
            eventName = get(EventKey.EventName.raw).toString().toEventName(),
            indexName = get(EventKey.IndexName.raw).toString().toIndexName(),
            userToken = getValue(EventKey.UserToken.raw)?.toString()?.toUserToken(),
            timestamp = (getValue(EventKey.Timestamp.raw) as? Long),
            queryID = getValue(EventKey.QueryID.raw)?.toString()?.toQueryID(),
            positions = (get(EventKey.Positions.raw) as? List<Int>),
            resources = resourcesOrNull(this),
        )
    }

    private fun InsightsEventDO.toConversionEvent(): InsightsEvent.Conversion {
        return InsightsEvent.Conversion(
            eventName = get(EventKey.EventName.raw).toString().toEventName(),
            indexName = get(EventKey.IndexName.raw).toString().toIndexName(),
            userToken = getValue(EventKey.UserToken.raw)?.toString()?.toUserToken(),
            timestamp = (getValue(EventKey.Timestamp.raw) as? Long),
            queryID = getValue(EventKey.QueryID.raw)?.toString()?.toQueryID(),
            resources = resourcesOrNull(this),
        )
    }

    private fun InsightsEventDO.toViewEvent(): InsightsEvent.View {
        return InsightsEvent.View(
            eventName = get(EventKey.EventName.raw).toString().toEventName(),
            indexName = get(EventKey.IndexName.raw).toString().toIndexName(),
            userToken = getValue(EventKey.UserToken.raw)?.toString()?.toUserToken(),
            timestamp = (getValue(EventKey.Timestamp.raw) as? Long),
            queryID = getValue(EventKey.QueryID.raw)?.toString()?.toQueryID(),
            resources = resourcesOrNull(this),
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun resourcesOrNull(map: InsightsEventDO): InsightsEvent.Resources? {
        return when {
            map.containsKey(EventKey.ObjectIds.raw) -> {
                val objectIDs = (map[EventKey.ObjectIds.raw] as List<String>).map { it.toObjectID() }
                InsightsEvent.Resources.ObjectIDs(objectIDs)
            }
            map.containsKey(EventKey.Filters.raw) -> {
                val filters = (map[EventKey.Filters.raw] as List<Map<String, String>>).map { getFilterFacet(it) }
                InsightsEvent.Resources.Filters(filters)
            }
            else -> null
        }
    }

    private fun getFilterFacet(map: FilterFacetDO): Filter.Facet {
        val attribute = map.getValue(FacetKey.Attribute.raw).toString().toAttribute()
        val isNegated = map.getValue(FacetKey.IsNegated.raw) as Boolean
        val valueType = ValueType.of((map.getValue(FacetKey.ValueType.raw) as String))
        val value = map.getValue(FacetKey.Value.raw)
        val score = map[FacetKey.Score.raw] as Int?
        return when (valueType) {
            ValueType.String -> Filter.Facet(
                attribute = attribute,
                value = value as String,
                isNegated = isNegated,
                score = score)
            ValueType.Boolean -> Filter.Facet(attribute = attribute,
                value = value as Boolean,
                isNegated = isNegated,
                score = score)
            ValueType.Number -> Filter.Facet(attribute = attribute,
                value = value as Number,
                isNegated = isNegated,
                score = score)
        }
    }
}
