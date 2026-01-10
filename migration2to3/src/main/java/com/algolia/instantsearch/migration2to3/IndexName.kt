package com.algolia.instantsearch.migration2to3

import java.net.URLEncoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object Route {
    const val EventsV1: String = "1/events"
}


public fun String.toIndexName(): IndexName {
    return this
}

public typealias IndexName = String
