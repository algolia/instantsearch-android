@file:OptIn(InternalSerializationApi::class)

package com.algolia.instantsearch.insights.internal.data.local.mapper

import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.Click
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.Conversion
import com.algolia.instantsearch.insights.internal.data.local.model.InsightsEventDO.EventType.View
import com.algolia.instantsearch.migration2to3.InsightsEvent
import kotlinx.serialization.InternalSerializationApi

internal object InsightsEventsMapper : Mapper<InsightsEvent, InsightsEventDO> {

    override fun map(input: InsightsEvent): InsightsEventDO {
        val builder = InsightsEventDO.Builder()
        builder.eventType(input)
        builder.eventName = input.eventName
        builder.indexName = input.indexName
        builder.timestamp = input.timestamp
        builder.queryID = input.queryID
        builder.userToken = input.userToken
        builder.resources(input)
        return builder.build()
    }

    private fun InsightsEventDO.Builder.resources(input: InsightsEvent) {
        when (val resources = input.resources) {
            is InsightsEvent.Resources.ObjectIDs -> objectIDs = resources.objectIDs
            is InsightsEvent.Resources.Filters -> filters = resources.filters.map { FilterFacetMapper.map(it) }
            else -> Unit
        }
    }

    private fun InsightsEventDO.Builder.eventType(input: InsightsEvent) {
        when (input) {
            is InsightsEvent.View -> eventType = View
            is InsightsEvent.Conversion -> eventType = Conversion
            is InsightsEvent.Click -> {
                eventType = Click
                positions = input.positions
            }
        }
    }

    override fun unmap(input: InsightsEventDO): InsightsEvent {
        return when (input.eventType) {
            Click -> input.toClickEvent()
            View -> input.toViewEvent()
            Conversion -> input.toConversionEvent()
        }
    }

    private fun InsightsEventDO.toClickEvent(): InsightsEvent.Click {
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

    private fun InsightsEventDO.toViewEvent(): InsightsEvent.View {
        return InsightsEvent.View(
            eventName = eventName,
            indexName = indexName,
            userToken = userToken,
            timestamp = timestamp,
            queryID = queryID,
            resources = resources()
        )
    }

    private fun InsightsEventDO.toConversionEvent(): InsightsEvent.Conversion {
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
