package com.algolia.instantsearch.migration2to3

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean

internal val JsonNonStrict = Json {
    ignoreUnknownKeys = true
    isLenient = true
    allowSpecialFloatingPointValues = true
    encodeDefaults = true
}

@Serializable(IgnorePlurals.Companion::class)
public sealed class IgnorePlurals {

    /**
     * Enables the ignore plurals functionality, where singulars and plurals are considered equivalent (foot = feet).
     * The languages supported here are either every language or those set by [Settings.queryLanguages]
     */
    public object True : IgnorePlurals()

    /**
     * Which disables ignore plurals, where singulars and plurals are not considered the same for matching purposes
     * (foot will not find feet).
     */
    public object False : IgnorePlurals()

    /**
     * A list of [Language] for which ignoring plurals should be enabled.
     * This list of [queryLanguages] will override any values that you may have set in [Settings.queryLanguages].
     */
    public data class QueryLanguages(val queryLanguages: List<Language>) : IgnorePlurals() {

        public constructor(vararg queryLanguage: Language) : this(queryLanguage.toList())
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Serializer(IgnorePlurals::class)
    public companion object : KSerializer<IgnorePlurals> {

        override fun serialize(encoder: Encoder, value: IgnorePlurals) {
            when (value) {
                is True -> Boolean.serializer().serialize(encoder, true)
                is False -> Boolean.serializer().serialize(encoder, false)
                is QueryLanguages -> ListSerializer(Language).serialize(encoder, value.queryLanguages)
            }
        }

        override fun deserialize(decoder: Decoder): IgnorePlurals {
            return when (val element = decoder.asJsonInput()) {
                is JsonArray -> QueryLanguages(
                    element.map {
                        JsonNonStrict.decodeFromJsonElement(Language, it)
                    }
                )
                is JsonPrimitive -> if (element.boolean) True else False
                else -> throw Exception()
            }
        }
    }
}
