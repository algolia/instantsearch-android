package com.algolia.instantsearch.searcher.multi.internal.types

import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.client.model.search.SearchResponse
import com.algolia.client.model.search.SearchResult
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject

internal fun Decoder.asJsonDecoder() = this as JsonDecoder
internal fun Encoder.asJsonOutput() = this as JsonEncoder
internal fun Decoder.asJsonInput() = asJsonDecoder().decodeJsonElement()

@InternalSerializationApi @Serializable
public data class ResponseMultiSearch(
    /**
     * List of result in the order they were submitted, one element for each [IndexedQuery].
     */
    @SerialName("results") public val results: List<ResultMultiSearch<*>>
)

/**
 * Multi search query response.
 */
@Serializable(ResultMultiSearchDeserializer::class)
public sealed interface ResultMultiSearch<T : SearchResult> {

    /** Actual search response */
    public val response: T

    /** Response for hits search */
    public data class Hits(override val response: SearchResponse) : ResultMultiSearch<SearchResponse>

    /** Response for facets search */
    public data class Facets(override val response: SearchForFacetValuesResponse) :
        ResultMultiSearch<SearchForFacetValuesResponse>
}

/**
 * [ResultMultiSearch] serializer.
 */
internal class ResultMultiSearchDeserializer<T : SearchResult>(dataSerializer: KSerializer<SearchResult>) :
    KSerializer<ResultMultiSearch<T>> {

    override val descriptor = dataSerializer.descriptor

    override fun deserialize(decoder: Decoder): ResultMultiSearch<T> {
        val json = decoder.asJsonDecoder().json
        val jsonObject = decoder.asJsonInput().jsonObject
        return multiSearchResult(json, jsonObject)
    }

    @Suppress("UNCHECKED_CAST")
    private fun multiSearchResult(json: Json, jsonObject: JsonObject): ResultMultiSearch<T> {
        return if (jsonObject.keys.contains("facetHits")) {
            ResultMultiSearch.Facets(json.decodeFromJsonElement(SearchForFacetValuesResponse.serializer(), jsonObject))
        } else {
            ResultMultiSearch.Hits(json.decodeFromJsonElement(SearchResponse.serializer(), jsonObject))
        } as ResultMultiSearch<T>
    }

    override fun serialize(encoder: Encoder, value: ResultMultiSearch<T>) {
        val json = encoder.asJsonOutput().json
        when (value) {
            is ResultMultiSearch.Hits -> json.encodeToString(SearchResponse.serializer(), value.response)
            is ResultMultiSearch.Facets -> json.encodeToString(SearchForFacetValuesResponse.serializer(), value.response)
        }
    }
}

