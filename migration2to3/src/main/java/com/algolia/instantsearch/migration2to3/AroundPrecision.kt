package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder

internal fun Decoder.asJsonDecoder() = this as JsonDecoder
internal fun Encoder.asJsonOutput() = this as JsonEncoder
internal fun Decoder.asJsonInput() = asJsonDecoder().decodeJsonElement()

