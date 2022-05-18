package com.algolia.instantsearch.searcher.multi.internal

import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.response.ResultSearch

internal data class MultiSearchOperation<out Request : IndexedQuery, Response : ResultSearch>(
    val requests: List<Request>,
    val completion: (List<Response>) -> Unit,
    val shouldTrigger: Boolean
)
