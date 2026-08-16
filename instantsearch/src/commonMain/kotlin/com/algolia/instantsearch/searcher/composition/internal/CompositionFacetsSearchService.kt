package com.algolia.instantsearch.searcher.composition.internal

import com.algolia.client.api.CompositionClient
import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.client.model.search.SearchParamsObject
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.searcher.multi.internal.SearchService
import com.algolia.client.model.composition.SearchForFacetValuesParams as CompositionSearchForFacetValuesParams
import com.algolia.client.model.composition.SearchForFacetValuesRequest as CompositionSearchForFacetValuesRequest

/**
 * Search service for facet values using the Algolia Composition API.
 */
internal interface CompositionFacetsSearchService :
    SearchService<CompositionFacetsSearchService.Request, SearchForFacetValuesResponse> {

    /**
     * Client to perform composition operations.
     */
    val client: CompositionClient

    /**
     * Composition facets service's request.
     */
    data class Request(
        val compositionID: String,
        val query: SearchParamsObject,
        val facetAttribute: String,
        val facetQuery: String?,
        val maxFacetHits: Int?,
    )
}

/**
 * Default implementation of [CompositionFacetsSearchService].
 */
internal class DefaultCompositionFacetsSearchService(
    override val client: CompositionClient,
) : CompositionFacetsSearchService {

    override suspend fun search(
        request: CompositionFacetsSearchService.Request,
        requestOptions: RequestOptions?,
    ): SearchForFacetValuesResponse {
        val response = client.searchForFacetValues(
            compositionID = request.compositionID,
            facetName = request.facetAttribute,
            searchForFacetValuesRequest = CompositionSearchForFacetValuesRequest(
                params = CompositionSearchForFacetValuesParams(
                    query = request.facetQuery,
                    maxFacetHits = request.maxFacetHits,
                    searchQuery = request.query.toCompositionParams(),
                )
            ),
            requestOptions = requestOptions,
        )
        val result = response.results?.firstOrNull()
            ?: error("The composition facet values search response contains no results")
        return result.toSearchForFacetValuesResponse()
    }
}
