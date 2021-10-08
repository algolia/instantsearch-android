package com.algolia.instantsearch.helper.searcher.multi.internal

import com.algolia.search.transport.RequestOptions

internal interface SearchService<Request, Result> {

    suspend fun search(request: Request, requestOptions: RequestOptions? = null): Result
}
