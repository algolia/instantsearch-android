package com.algolia.instantsearch.helper.searcher.multi.internal

import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseMultiSearch
import com.algolia.search.transport.RequestOptions

internal class MultiSearchService(
    private val client: ClientSearch
) : SearchService<MultiSearchService.Request, ResponseMultiSearch> {

    data class Request(
        val queries: List<IndexedQuery>,
        val strategy: MultipleQueriesStrategy? = null,
    )

    override suspend fun search(request: Request, requestOptions: RequestOptions?): ResponseMultiSearch {
        return client.search(request.queries, request.strategy, requestOptions)
    }
}
