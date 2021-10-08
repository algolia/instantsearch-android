package com.algolia.instantsearch.helper.searcher.multi.internal

import com.algolia.instantsearch.helper.searcher.multi.MultiSearchComponent
import com.algolia.search.model.multipleindex.IndexedQuery
import com.algolia.search.model.response.ResultSearch

@Suppress("UNCHECKED_CAST")
internal fun <T : IndexedQuery, K : ResultSearch> MultiSearchComponent<T, K>.asMultiSearchComponent() =
    this as MultiSearchComponent<IndexedQuery, ResultSearch>
