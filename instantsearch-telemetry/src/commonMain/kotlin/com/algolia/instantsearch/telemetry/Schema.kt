package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.Internal
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoNumber

@Internal
@Serializable
public data class Schema(
    @ProtoNumber(700) val components: List<Component> = emptyList()
)

@Internal
public fun Schema.toByteArray(): ByteArray {
    return ProtoBuf.encodeToByteArray(Schema.serializer(), this)
}
