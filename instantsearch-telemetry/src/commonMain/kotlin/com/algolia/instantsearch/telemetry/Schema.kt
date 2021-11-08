package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.InternalInstantSearch
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber

@InternalInstantSearch
@Serializable
public data class Schema(
    @ProtoNumber(700) val components: List<Component> = emptyList()
)

@InternalInstantSearch
public fun Schema.toByteArray(): ByteArray {
    return ProtoBuf.encodeToByteArray(Schema.serializer(), this)
}
