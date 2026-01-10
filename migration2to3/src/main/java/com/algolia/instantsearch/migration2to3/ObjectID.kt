package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

public fun String.toObjectID(): ObjectID {
    return ObjectID(this)
}

@Serializable(ObjectID.Companion::class)
public data class ObjectID(@SerialName(Key.ObjectID) override val raw: String) : Raw<String> {

    init {
        if (raw.isBlank()) throw EmptyStringException(Key.ObjectID)
    }

    override fun toString(): String {
        return raw
    }

    public companion object : KSerializer<ObjectID> {

        private val serializer = String.serializer()

        override val descriptor: SerialDescriptor = serializer.descriptor

        override fun serialize(encoder: Encoder, value: ObjectID) {
            serializer.serialize(encoder, value.raw)
        }

        override fun deserialize(decoder: Decoder): ObjectID {
            return serializer.deserialize(decoder).toObjectID()
        }
    }
}
