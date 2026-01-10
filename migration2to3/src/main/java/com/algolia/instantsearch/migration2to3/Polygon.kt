package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(Polygon.Companion::class)
public data class Polygon(
    val point1: Point,
    val point2: Point,
    val point3: Point,
    private val points: List<Point>
) : Raw<List<Float>> {

    public constructor(point1: Point, point2: Point, point3: Point, vararg points: Point) : this(
        point1,
        point2,
        point3,
        points.toList()
    )

    public operator fun get(index: Int): Point {
        return when (index) {
            0 -> point1
            1 -> point2
            2 -> point3
            else -> points[index - 3]
        }
    }

    override val raw: List<Float> = listOf(
        *point1.raw.toTypedArray(),
        *point2.raw.toTypedArray(),
        *point3.raw.toTypedArray(),
        *points.flatMap { it.raw }.toTypedArray()
    )

    public companion object : KSerializer<Polygon> {

        private val serializer = ListSerializer(Float.serializer())

        override val descriptor: SerialDescriptor = serializer.descriptor

        override fun serialize(encoder: Encoder, value: Polygon) {
            serializer.serialize(encoder, value.raw)
        }

        override fun deserialize(decoder: Decoder): Polygon {
            val floats = serializer.deserialize(decoder)

            return Polygon(
                floats[0] and floats[1],
                floats[2] and floats[3],
                floats[4] and floats[5],
                (6 until floats.size step 2).map {
                    floats[it] and floats[it + 1]
                }
            )
        }
    }
}

public infix fun Float.and(longitude: Float): Point {
    return Point(this, longitude)
}
