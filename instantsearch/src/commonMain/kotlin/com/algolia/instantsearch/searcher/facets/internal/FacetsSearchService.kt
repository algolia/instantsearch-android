@file:OptIn(InternalSerializationApi::class)

package com.algolia.instantsearch.searcher.facets.internal

import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.SearchForFacetValuesRequest
import com.algolia.client.model.search.SearchForFacetValuesResponse
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.searcher.multi.internal.types.FacetIndexQuery
import com.algolia.instantsearch.searcher.multi.internal.SearchService
import kotlinx.serialization.InternalSerializationApi

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
        // Convert SearchParamsObject to params string for v3 API
        // For now, we'll pass null for params and use facetQuery directly
        val facetRequest = SearchForFacetValuesRequest(
            params = null, // TODO: Convert SearchParamsObject to params string if needed
            facetQuery = request.facetQuery
        )
        return client.searchForFacetValues(
            indexName = request.indexName,
            facetName = request.facetAttribute,
            searchForFacetValuesRequest = facetRequest,
            requestOptions = requestOptions
        )
    }
}
