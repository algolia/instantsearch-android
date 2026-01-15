package com.algolia.instantsearch.insights.internal.data.local.model

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@InternalSerializationApi
@Serializable
internal data class InsightsEventDO(
    @SerialName("eventType") val eventType: EventType,
    @SerialName("eventName") val eventName: String,
    @SerialName("index") val indexName: String,
    @SerialName("userToken") val userToken: String? = null,
    @SerialName("timestamp") val timestamp: Long? = null,
    @SerialName("queryID") val queryID: String? = null,
    @SerialName("objectIDs") val objectIDs: List<String>? = null,
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
        var eventName: String? = null
        var indexName: String? = null
        var userToken: String? = null
        var timestamp: Long? = null
        var queryID: String? = null
        var objectIDs: List<String>? = null
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
