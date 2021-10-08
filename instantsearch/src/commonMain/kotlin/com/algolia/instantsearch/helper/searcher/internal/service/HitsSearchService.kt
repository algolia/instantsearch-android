package com.algolia.instantsearch.helper.searcher.internal.service

import com.algolia.search.client.ClientSearch
import com.algolia.search.model.IndexName
import com.algolia.search.model.response.ResultSearch
import com.algolia.search.model.search.Query
import com.algolia.search.transport.RequestOptions

internal class HitsSearchService<Response : ResultSearch>(
    val client: ClientSearch,
    val isDisjunctiveFacetingEnabled: Boolean = true
) : SearchService<HitsSearchService.Request, Response> {

    internal val factory = IndexQueriesFactory()

    override suspend fun search(request: Request): Response {
        TODO()
    }

    fun collect(request: Request) {
        TODO()
    }

    data class Request(
        val indexName: IndexName,
        val query: Query,
        val requestOptions: RequestOptions? = null
    )
}
