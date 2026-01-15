@file:OptIn(InternalSerializationApi::class)

package com.algolia.instantsearch.searcher.facets.internal

import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.SearchResponse
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.migration2to3.FacetIndexQuery
import com.algolia.instantsearch.searcher.multi.internal.SearchService
import kotlinx.serialization.InternalSerializationApi

/**
 * Search service for facets.
 */
internal interface FacetsSearchService : SearchService<FacetIndexQuery, SearchResponse> {

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

    override suspend fun search(request: FacetIndexQuery, requestOptions: RequestOptions?): SearchResponse {
        return client.searchSingleIndex(
            indexName = TODO(),
            searchParams = TODO(),
            requestOptions = TODO()
        )
    }
}
