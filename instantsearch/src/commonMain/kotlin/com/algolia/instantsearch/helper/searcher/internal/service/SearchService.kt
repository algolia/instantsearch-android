package com.algolia.instantsearch.helper.searcher.internal.service

/**
 * Represents a search service.
 */
internal interface SearchService<Request, Response> {

    /**
     * Launch a search operation.
     *
     * @param request search request
     * @return search response
     */
    suspend fun search(request: Request): Response
}
