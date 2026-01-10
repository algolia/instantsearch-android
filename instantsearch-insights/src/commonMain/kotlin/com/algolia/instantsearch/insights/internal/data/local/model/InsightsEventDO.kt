package com.algolia.instantsearch.insights.internal.data.local.model

import com.algolia.instantsearch.migration2to3.EventName
import com.algolia.instantsearch.migration2to3.IndexName
import com.algolia.instantsearch.migration2to3.QueryID
import com.algolia.instantsearch.migration2to3.UserToken
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
internal data class InsightsEventDO(
    @SerialName("eventType") val eventType: EventType,
    @SerialName("eventName") val eventName: EventName,
    @SerialName("index") val indexName: IndexName,
    @SerialName("userToken") val userToken: UserToken? = null,
    @SerialName("timestamp") val timestamp: Long? = null,
    @SerialName("queryID") val queryID: QueryID? = null,
    @SerialName("objectIDs") val objectIDs: List<ObjectID>? = null,
    @SerialName("positions") val positions: List<Int>? = null,
    @SerialName("filters") val filters: List<FilterFacetDO>? = null
) {

    @Serializable
    internal enum class EventType {
        @SerialName("click")
        Click,

        @SerialName("view")
        View,

        @SerialName("conversion")
        Conversion;
    }

    class Builder {
        var eventType: EventType? = null
        var eventName: EventName? = null
        var indexName: IndexName? = null
        var userToken: UserToken? = null
        var timestamp: Long? = null
        var queryID: QueryID? = null
        var objectIDs: List<ObjectID>? = null
        var positions: List<Int>? = null
        var filters: List<FilterFacetDO>? = null

        fun build(): InsightsEventDO = InsightsEventDO(
            eventType ?: error("eventType can't not be null"),
            eventName ?: error("eventName can't not be null"),
            indexName ?: error("indexName can't not be null"),
            userToken,
            timestamp,
            queryID,
            objectIDs,
            positions,
            filters
        )
    }
}
