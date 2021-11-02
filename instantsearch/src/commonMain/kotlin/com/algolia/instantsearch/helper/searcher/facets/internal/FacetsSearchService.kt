package com.algolia.instantsearch.helper.searcher.facets.internal

import com.algolia.instantsearch.helper.searcher.multi.internal.SearchService
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.FacetIndexQuery
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.transport.RequestOptions

/**
 * Search service for facets.
 */
internal class FacetsSearchService(
    val client: ClientSearch
) : SearchService<FacetIndexQuery, ResponseSearchForFacets> {

    override suspend fun search(request: FacetIndexQuery, requestOptions: RequestOptions?): ResponseSearchForFacets {
        val index = client.initIndex(request.indexName)
        return index.searchForFacets(request.facetAttribute, request.facetQuery, request.query, requestOptions)
    }
}
