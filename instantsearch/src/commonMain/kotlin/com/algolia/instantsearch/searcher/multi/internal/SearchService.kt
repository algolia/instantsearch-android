package com.algolia.instantsearch.searcher.multi.internal

import com.algolia.client.transport.RequestOptions

/**
 * Service for search operations.
 */
internal interface SearchService<Request, Result> {

    /**
     * Runs a search operation.
     *
     * @param request search request
     * @return search response
     */
    suspend fun search(request: Request, requestOptions: RequestOptions? = null): Result
}
