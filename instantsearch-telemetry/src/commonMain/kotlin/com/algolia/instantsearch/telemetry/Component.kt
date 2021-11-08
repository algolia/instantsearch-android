package com.algolia.instantsearch.telemetry

import com.algolia.instantsearch.InternalInstantSearch
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@InternalInstantSearch
@Serializable
public data class Component(
    @ProtoNumber(600) val type: ComponentType,
    @ProtoNumber(601) val parameters: List<ComponentParam> = emptyList(),
    @ProtoNumber(602) val isConnector: Boolean
)
