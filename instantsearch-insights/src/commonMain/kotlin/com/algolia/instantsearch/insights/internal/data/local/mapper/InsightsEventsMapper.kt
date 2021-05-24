package com.algolia.instantsearch.insights.internal.data.local.mapper

import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.Click
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.Conversion
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.View
import com.algolia.search.model.insights.InsightsEvent

internal object InsightsEventsMapper : Mapper<InsightsEvent, InsightsEventDO> {

    override fun map(input: InsightsEvent): InsightsEventDO {
        val builder = InsightsEventDO.Builder()
        when (input) {
            is InsightsEvent.View -> builder.eventType = View
            is InsightsEvent.Conversion -> builder.eventType = Conversion
            is InsightsEvent.Click -> {
                builder.eventType = Click
                builder.positions = input.positions
            }
        }
        builder.eventName = input.eventName
        builder.indexName = input.indexName
        builder.timestamp = input.timestamp
        builder.queryID = input.queryID
        builder.userToken = input.userToken
        when (val resources = input.resources) {
            is InsightsEvent.Resources.ObjectIDs -> builder.objectIDs = resources.objectIDs
            is InsightsEvent.Resources.Filters -> builder.filters = resources.filters.map { FilterFacetMapper.map(it) }
        }
        return builder.build()
    }

    override fun unmap(input: InsightsEventDO): InsightsEvent {
        return when (input.eventType) {
            Click -> input.toClick()
            View -> input.toView()
            Conversion -> input.toConversion()
        }
    }

    private fun InsightsEventDO.toClick(): InsightsEvent.Click {
        return InsightsEvent.Click(
            eventName = eventName,
            indexName = indexName,
            userToken = userToken,
            timestamp = timestamp,
            queryID = queryID,
            positions = positions,
            resources = resources()
        )
    }

    private fun InsightsEventDO.toView(): InsightsEvent.View {
        return InsightsEvent.View(
            eventName = eventName,
            indexName = indexName,
            userToken = userToken,
            timestamp = timestamp,
            queryID = queryID,
            resources = resources()
        )
    }

    private fun InsightsEventDO.toConversion(): InsightsEvent.Conversion {
        return InsightsEvent.Conversion(
            eventName = eventName,
            indexName = indexName,
            userToken = userToken,
            timestamp = timestamp,
            queryID = queryID,
            resources = resources()
        )
    }

    private fun InsightsEventDO.resources(): InsightsEvent.Resources? {
        if (objectIDs != null) return InsightsEvent.Resources.ObjectIDs(objectIDs)
        if (filters != null) return InsightsEvent.Resources.Filters(filters.map { FilterFacetMapper.unmap(it) })
        return null
    }
}
