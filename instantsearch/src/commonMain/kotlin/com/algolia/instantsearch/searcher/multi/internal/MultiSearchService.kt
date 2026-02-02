package com.algolia.instantsearch.searcher.multi.internal

import com.algolia.client.api.SearchClient
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.searcher.multi.internal.types.IndexedQuery
import com.algolia.instantsearch.searcher.multi.internal.types.MultipleQueriesStrategy
import com.algolia.instantsearch.searcher.multi.internal.types.ResponseMultiSearch
import com.algolia.instantsearch.searcher.internal.search
import com.algolia.instantsearch.searcher.multi.internal.MultiSearchService.Request

/**
 * Search service for multi search operations.
 */
internal interface MultiSearchService : SearchService<Request, ResponseMultiSearch> {

    /**
     * Client to perform search operations.
     */
    val client: SearchClient

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
internal class DefaultMultiSearchService(override val client: SearchClient) : MultiSearchService {

    override suspend fun search(
        request: Request,
        requestOptions: RequestOptions?
    ): ResponseMultiSearch {
        return client.search(request.queries, request.strategy, requestOptions)
    }
}
