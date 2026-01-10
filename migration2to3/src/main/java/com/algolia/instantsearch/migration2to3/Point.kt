package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(Point.Companion::class)
public data class Point(
    val latitude: Float,
    val longitude: Float
) : Raw<List<Float>> {

    override val raw: List<Float> = listOf(latitude, longitude)

    public companion object : KSerializer<Point> {

        private val serializer = ListSerializer(Float.serializer())

        override val descriptor: SerialDescriptor = serializer.descriptor

        override fun serialize(encoder: Encoder, value: Point) {
            serializer.serialize(encoder, value.raw)
        }

        override fun deserialize(decoder: Decoder): Point {
            val floats = serializer.deserialize(decoder)

            return Point(floats[0], floats[1])
        }
    }
}
