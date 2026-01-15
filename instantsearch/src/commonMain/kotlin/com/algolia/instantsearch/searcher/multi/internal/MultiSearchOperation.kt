package com.algolia.instantsearch.searcher.multi.internal

import com.algolia.client.model.search.SearchResult
import com.algolia.instantsearch.migration2to3.IndexedQuery

internal data class MultiSearchOperation<out Request : IndexedQuery, Response : SearchResult>(
    val requests: List<Request>,
    val completion: (List<Response>) -> Unit,
    val shouldTrigger: Boolean
)
