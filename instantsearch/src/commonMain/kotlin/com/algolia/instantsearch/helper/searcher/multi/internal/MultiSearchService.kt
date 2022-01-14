package com.algolia.instantsearch.helper.searcher.multi.internal

import com.algolia.instantsearch.helper.searcher.multi.internal.MultiSearchService.Request
import com.algolia.search.client.ClientSearch
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.multipleindex.MultipleQueriesStrategy
import com.algolia.search.model.response.ResponseMultiSearch
import com.algolia.search.transport.RequestOptions

/**
 * Search service for multi search operations.
 */
internal interface MultiSearchService : SearchService<Request, ResponseMultiSearch> {

    /**
     * Client to perform search operations.
     */
    val client: ClientSearch

    /**
     * Request for multi search operations.
     */
    data class Request(
        val queries: List<IndexedQuery>,
        val strategy: MultipleQueriesStrategy? = null,
    )
}

/**
 * Default implementation of [MultiSearchService].
 */
internal class DefaultMultiSearchService(override val client: ClientSearch) : MultiSearchService {

    override suspend fun search(
        request: Request,
        requestOptions: RequestOptions?
    ): ResponseMultiSearch {
        return client.search(request.queries, request.strategy, requestOptions)
    }
}
