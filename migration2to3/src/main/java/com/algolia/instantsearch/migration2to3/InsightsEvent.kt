package com.algolia.instantsearch.migration2to3

import com.algolia.client.model.insights.ObjectData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.put

// InsightsEvent.View ->  com.algolia.client.model.insights.ViewEvent
// InsightsEvent.Click ->  com.algolia.client.model.insights.ClickEvent



@Serializable(InsightsEvent.Companion::class)
public sealed class InsightsEvent {

    public abstract val eventName: EventName
    public abstract val indexName: IndexName
    public abstract val userToken: UserToken?
    public abstract val timestamp: Long?
    public abstract val queryID: QueryID?
    public abstract val resources: Resources?

    public data class View(
        override val eventName: EventName,
        override val indexName: IndexName,
        override val userToken: UserToken? = null,
        override val timestamp: Long? = null,
        override val queryID: QueryID? = null,
        override val resources: Resources? = null,
    ) : InsightsEvent()

    public data class Click(
        override val eventName: EventName,
        override val indexName: IndexName,
        override val userToken: UserToken? = null,
        override val timestamp: Long? = null,
        override val queryID: QueryID? = null,
        override val resources: Resources? = null,
        val positions: List<Int>? = null,
    ) : InsightsEvent() {

        init {
            if (queryID != null && positions == null)
                throw IllegalArgumentException("Positions are required for a Click event when a queryID is provided")
        }
    }

    public data class Conversion(
        override val eventName: EventName,
        override val indexName: IndexName,
        override val userToken: UserToken? = null,
        override val timestamp: Long? = null,
        override val queryID: QueryID? = null,
        override val resources: Resources? = null,
        val objectData: List<ObjectData>? = null
    ) : InsightsEvent()

    public sealed class Resources {

        public data class ObjectIDs(val objectIDs: List<ObjectID>) : Resources() {

            init {
                if (objectIDs.size > 20)
                    throw IllegalArgumentException("You can't send more than 20 objectIDs for a single event at a time.")
            }
        }

        public data class Filters(val filters: List<Filter.Facet>) : Resources() {

            init {
                if (filters.size > 10)
                    throw IllegalArgumentException("You can't send more than 10 filters for a single event at at time.")
            }
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(InsightsEvent::class)
    public companion object : KSerializer<InsightsEvent> {

        private infix fun JsonObjectBuilder.stringify(resources: Resources?) {
            if (resources == null) return
            when (resources) {
                is Resources.ObjectIDs -> put(
                    Key.ObjectIDs,
                    buildJsonArray { resources.objectIDs.forEach { add(it.raw) } }
                )
                is Resources.Filters -> put(
                    Key.Filters,
                    buildJsonArray {
                        resources.filters.forEach { filter ->
                            FilterConverter.Legacy(filter).forEach { add(it) }
                        }
                    }
                )
            }
        }

        private infix fun JsonObjectBuilder.eventType(event: InsightsEvent) {
            put(
                Key.EventType,
                when (event) {
                    is Click -> Key.Click
                    is View -> Key.View
                    is Conversion -> Key.Conversion
                }
            )
        }

        override fun serialize(encoder: Encoder, value: InsightsEvent) {
            val json = buildJsonObject {
                this eventType value
                put(Key.EventName, value.eventName)
                value.timestamp?.let { put(Key.Timestamp, it) }
                put(Key.Index, value.indexName)
                value.userToken?.let { put(Key.UserToken, it) }
                value.queryID?.let { put(Key.QueryID, it) }
                this stringify value.resources
                if (value is Click) {
                    value.positions?.let {
                        put(
                            Key.Positions,
                            buildJsonArray { it.forEach { add((it as Number)) } }
                        )
                    }
                }
                if (value is Conversion) {
                    value.objectData?.let {
                        put(
                            Key.ObjectData,
                            buildJsonArray {
                                it.forEach {
                                    add(Json {  }.encodeToJsonElement(ObjectData.serializer(), it))
                                }
                            }
                        )

                    }
                }
            }
            encoder.asJsonOutput().encodeJsonElement(json)
        }

        override fun deserialize(decoder: Decoder): InsightsEvent {
            throw UnsupportedOperationException("Insight event deserialization is not an expected operation")
        }
    }
}
