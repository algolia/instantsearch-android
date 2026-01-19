package com.algolia.instantsearch.searcher.multi.internal.types

import com.algolia.client.model.search.SearchParamsObject
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(MultipleQueriesStrategy.Companion::class)
public sealed class MultipleQueriesStrategy(override val raw: String) : Raw<String> {

    /**
     * Execute the sequence of queries until the end. This is recommended when each query is of equal importance,
     * meaning all records of all queries need to be returned.
     */
    public object None : MultipleQueriesStrategy(Key.None)

    /**
     * Execute queries one by one, but stop as soon as the cumulated number of hits is at least [SearchParamsObject.hitsPerPage].
     * This is recommended when each query is an alternative, and where, if the first returns enough records,
     * there is no need to perform the remaining queries.
     */
    public object StopIfEnoughMatches : MultipleQueriesStrategy(Key.StopIfEnoughMatches)

    public data class Other(override val raw: String) : MultipleQueriesStrategy(raw)

    override fun toString(): String {
        return raw
    }

    public companion object : KSerializer<MultipleQueriesStrategy> {

        private val serializer = String.serializer()

        override val descriptor: SerialDescriptor = serializer.descriptor

        override fun serialize(encoder: Encoder, value: MultipleQueriesStrategy) {
            serializer.serialize(encoder, value.raw)
        }

        override fun deserialize(decoder: Decoder): MultipleQueriesStrategy {
            return when (val string = serializer.deserialize(decoder)) {
                Key.None -> None
                Key.StopIfEnoughMatches -> StopIfEnoughMatches
                else -> Other(string)
            }
        }
    }
}

internal interface Raw<T> {
    val raw: T
}

internal object Key {
    const val None = "none"
    const val StopIfEnoughMatches = "stopIfEnoughMatches"
    const val Results = "results"
    const val FacetHits = "facetHits"
}

