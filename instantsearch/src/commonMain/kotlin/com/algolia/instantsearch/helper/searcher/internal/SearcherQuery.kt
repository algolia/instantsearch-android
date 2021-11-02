package com.algolia.instantsearch.helper.searcher.internal

import com.algolia.instantsearch.helper.searcher.SearcherQuery
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Query

/**
 * Convenience implementation of [SearcherQuery] with [Query] and [ResponseSearch].
 */
internal typealias SearcherForHits = SearcherQuery<Query, ResponseSearch>

/**
 * Convenience implementation of [SearcherQuery] with [Query] and [ResponseSearchForFacets].
 */
internal typealias SearcherForFacets = SearcherQuery<Query, ResponseSearchForFacets>
