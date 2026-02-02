@file:OptIn(InternalSerializationApi::class)

package com.algolia.instantsearch.searcher.facets.internal

import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.SearchForFacetValuesRequest
import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.searcher.multi.internal.types.FacetIndexQuery
import com.algolia.instantsearch.searcher.multi.internal.SearchService
import io.ktor.http.encodeURLParameter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

/**
 * Search service for facets.
 */
internal interface FacetsSearchService : SearchService<FacetIndexQuery, SearchForFacetValuesResponse> {

    /**
     * Client to perform search operations.
     */
    val client: SearchClient
}

/**
 * Default implementation of [FacetsSearchService].
 */
internal class DefaultFacetsSearchService(
    override val client: SearchClient
) : FacetsSearchService {

    override suspend fun search(request: FacetIndexQuery, requestOptions: RequestOptions?): SearchForFacetValuesResponse {
        val params = request.query.toParamsString(client.options.json).ifBlank { null }
        val facetRequest = SearchForFacetValuesRequest(
            params = params,
            facetQuery = request.facetQuery
        )
        return client.searchForFacetValues(
            indexName = request.indexName,
            facetName = request.facetAttribute,
            searchForFacetValuesRequest = facetRequest,
            requestOptions = requestOptions
        )
    }

    private fun SearchParamsObject.toParamsString(json: Json): String {
        val jsonObject = json.encodeToJsonElement(SearchParamsObject.serializer(), this).jsonObject
        return jsonObject
            .mapNotNull { (key, value) ->
                if (value is JsonNull) return@mapNotNull null
                key to value.toQueryParamValue(json)
            }
            .sortedBy { it.first }
            .joinToString("&") { (key, value) -> "${key}=${value.encodeURLParameter()}" }
    }

    private fun JsonElement.toQueryParamValue(json: Json): String {
        return when (this) {
            is JsonPrimitive -> content
            is JsonArray, is JsonObject -> json.encodeToString(JsonElement.serializer(), this)
            is JsonNull -> ""
            else -> json.encodeToString(JsonElement.serializer(), this)
        }
    }
}
