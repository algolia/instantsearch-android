package com.algolia.instantsearch.telemetry

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
public data class Component(
    @ProtoNumber(600) val type: ComponentType,
    @ProtoNumber(601) val parameters: List<ComponentParam> = emptyList(),
    @ProtoNumber(602) val isConnector: Boolean
)
