package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(BoundingBox.Companion::class)
public data class BoundingBox(
    val point1: Point,
    val point2: Point
) : Raw<List<Float>> {

    override val raw: List<Float> = listOf(point1.latitude, point1.longitude, point2.latitude, point2.longitude)

    public companion object : KSerializer<BoundingBox> {

        private val serializer = Float.serializer()

        override val descriptor: SerialDescriptor = ListSerializer(serializer).descriptor

        override fun serialize(encoder: Encoder, value: BoundingBox) {
            ListSerializer(serializer).serialize(encoder, value.raw)
        }

        override fun deserialize(decoder: Decoder): BoundingBox {
            val floats = ListSerializer(serializer).deserialize(decoder)

            return BoundingBox(
                floats[0] and floats[1],
                floats[2] and floats[3]
            )
        }
    }
}
