package com.algolia.instantsearch.searcher.multi.internal

import com.algolia.instantsearch.migration2to3.IndexedQuery
import com.algolia.instantsearch.migration2to3.ResultSearch

internal data class MultiSearchOperation<out Request : IndexedQuery, Response : ResultSearch>(
    val requests: List<Request>,
    val completion: (List<Response>) -> Unit,
    val shouldTrigger: Boolean
)
