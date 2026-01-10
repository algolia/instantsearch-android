package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(ExplainModule.Companion::class)
public sealed class ExplainModule(override val raw: String) : Raw<String> {

    public object MatchAlternatives : ExplainModule(Key.MatchAlternatives)

    public data class Other(override val raw: String) : ExplainModule(raw)

    public companion object : KSerializer<ExplainModule> {

        private val serializer = String.serializer()

        override val descriptor: SerialDescriptor = serializer.descriptor

        override fun serialize(encoder: Encoder, value: ExplainModule) {
            serializer.serialize(encoder, value.raw)
        }

        override fun deserialize(decoder: Decoder): ExplainModule {
            return when (val string = serializer.deserialize(decoder)) {
                Key.MatchAlternatives -> MatchAlternatives
                else -> Other(string)
            }
        }
    }
}
