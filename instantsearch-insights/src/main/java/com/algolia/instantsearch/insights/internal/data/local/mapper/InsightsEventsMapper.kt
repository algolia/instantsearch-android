package com.algolia.instantsearch.insights.internal.data.local.mapper

import com.algolia.instantsearch.insights.internal.data.local.model.EventKey
import com.algolia.instantsearch.insights.internal.data.local.model.EventType
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.extension.put
import com.algolia.search.helper.toEventName
import com.algolia.search.helper.toIndexName
import com.algolia.search.helper.toQueryID
import com.algolia.search.helper.toUserToken
import com.algolia.search.model.insights.InsightsEvent

internal object InsightsEventsMapper : Mapper<InsightsEvent, InsightsEventDO> {

    override fun map(input: InsightsEvent): InsightsEventDO {
        return when (input) {
            is InsightsEvent.View -> input.toEventDO()
            is InsightsEvent.Conversion -> input.toEventDO()
            is InsightsEvent.Click -> input.toEventDO()
        }
    }

    private fun InsightsEvent.Click.toEventDO(): InsightsEventDO {
        return commonEventDO().also { map ->
            map[EventKey.EventType.raw] = EventType.Click.raw
            positions?.let { map[EventKey.Positions.raw] = it }
        }
    }

    private fun InsightsEvent.Conversion.toEventDO(): InsightsEventDO {
        return commonEventDO().also {
            it[EventKey.EventType.raw] = EventType.Conversion.raw
        }
    }

    private fun InsightsEvent.View.toEventDO(): InsightsEventDO {
        return commonEventDO().also {
            it[EventKey.EventType.raw] = EventType.View.raw
        }
    }

    private fun InsightsEvent.commonEventDO(): MutableMap<String, Any> {
        return mutableMapOf<String, Any>().also { map ->
            map[EventKey.EventName.raw] = eventName.raw
            map[EventKey.IndexName.raw] = indexName.raw
            timestamp?.let { map[EventKey.Timestamp.raw] = it }
            queryID?.let { map[EventKey.QueryID.raw] = it.raw }
            userToken?.let { map[EventKey.UserToken.raw] = it.raw }
            resources?.let { ResourcesMapper.map(it) }?.also { map.put(it) }
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
            eventName = getValue(EventKey.EventName.raw).toString().toEventName(),
            indexName = getValue(EventKey.IndexName.raw).toString().toIndexName(),
            userToken = get(EventKey.UserToken.raw)?.toString()?.toUserToken(),
            timestamp = (get(EventKey.Timestamp.raw) as? Long),
            queryID = get(EventKey.QueryID.raw)?.toString()?.toQueryID(),
            positions = get(EventKey.Positions.raw) as? List<Int>,
            resources = resourcesOrNull(this),
        )
    }

    private fun InsightsEventDO.toConversionEvent(): InsightsEvent.Conversion {
        return InsightsEvent.Conversion(
            eventName = getValue(EventKey.EventName.raw).toString().toEventName(),
            indexName = getValue(EventKey.IndexName.raw).toString().toIndexName(),
            userToken = get(EventKey.UserToken.raw)?.toString()?.toUserToken(),
            timestamp = get(EventKey.Timestamp.raw) as? Long,
            queryID = get(EventKey.QueryID.raw)?.toString()?.toQueryID(),
            resources = resourcesOrNull(this),
        )
    }

    private fun InsightsEventDO.toViewEvent(): InsightsEvent.View {
        return InsightsEvent.View(
            eventName = getValue(EventKey.EventName.raw).toString().toEventName(),
            indexName = getValue(EventKey.IndexName.raw).toString().toIndexName(),
            userToken = get(EventKey.UserToken.raw)?.toString()?.toUserToken(),
            timestamp = get(EventKey.Timestamp.raw) as? Long,
            queryID = get(EventKey.QueryID.raw)?.toString()?.toQueryID(),
            resources = resourcesOrNull(this),
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun resourcesOrNull(insightsEventDO: InsightsEventDO): InsightsEvent.Resources? {
        val objectsIDs = insightsEventDO[EventKey.ObjectIds.raw]
        if (objectsIDs != null) return ResourcesMapper.unmap(EventKey.ObjectIds.raw to objectsIDs)
        val filters = insightsEventDO[EventKey.Filters.raw]
        if (filters != null) return ResourcesMapper.unmap(EventKey.Filters.raw to filters)
        return null
    }
}
