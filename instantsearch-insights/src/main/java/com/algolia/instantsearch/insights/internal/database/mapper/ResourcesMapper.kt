package com.algolia.instantsearch.insights.internal.database.mapper

import com.algolia.instantsearch.insights.internal.database.model.EventKey
import com.algolia.search.helper.toObjectID
import com.algolia.search.model.insights.InsightsEvent

internal object ResourcesMapper : Mapper<InsightsEvent.Resources, Pair<String, Any>> {

    override fun map(input: InsightsEvent.Resources): Pair<String, Any> {
        return when (input) {
            is InsightsEvent.Resources.ObjectIDs -> EventKey.ObjectIds.raw to input.objectIDs.map { it.raw }
            is InsightsEvent.Resources.Filters -> EventKey.Filters.raw to input.filters.map { FilterFacetMapper.map(it) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun unmap(input: Pair<String, Any>): InsightsEvent.Resources {
        val (resourceType, value) = input
        return when (resourceType) {
            EventKey.ObjectIds.raw -> InsightsEvent.Resources.ObjectIDs(
                (value as List<String>).map { it.toObjectID() }
            )
            EventKey.Filters.raw -> InsightsEvent.Resources.Filters(
                (value as List<Map<String, String>>).map { FilterFacetMapper.unmap(it) }
            )
            else -> throw UnsupportedOperationException("Unknown Resources type: $resourceType")
        }
    }
}
