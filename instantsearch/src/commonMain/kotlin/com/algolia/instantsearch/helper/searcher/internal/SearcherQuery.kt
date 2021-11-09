package com.algolia.instantsearch.helper.searcher.internal

import com.algolia.instantsearch.helper.searcher.SearcherQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Query

// TODO: to be exposed as @Internal

/**
 * Convenience implementation of [SearcherQuery] with [Query] and [ResponseSearch].
 */
public typealias SearcherForHits<T> = SearcherQuery<T, ResponseSearch>

/**
 * Convenience implementation of [SearcherQuery] with [Query] and [ResponseSearchForFacets].
 */
public typealias SearcherForFacets<T> = SearcherQuery<T, ResponseSearchForFacets>
