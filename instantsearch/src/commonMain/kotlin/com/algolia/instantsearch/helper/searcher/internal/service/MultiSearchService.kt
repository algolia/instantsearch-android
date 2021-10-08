package com.algolia.instantsearch.helper.searcher.internal.service

import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseMultiSearch
import com.algolia.search.transport.RequestOptions

internal class MultiSearchService(
    private val client: ClientSearch
) : SearchService<MultiSearchService.Request, ResponseMultiSearch> {

    override suspend fun search(request: Request): ResponseMultiSearch {
        return client.search(request.queries, request.strategy, request.requestOptions)
    }

    data class Request(
        val queries: List<IndexedQuery>,
        val strategy: MultipleQueriesStrategy? = null,
        val requestOptions: RequestOptions? = null
    )
}
