package com.algolia.instantsearch.searcher

import com.algolia.search.model.params.CommonSearchParameters
import com.algolia.search.model.response.ResponseSearch

/**
 * Implementation of [SearcherQuery] with [CommonSearchParameters] and [ResponseSearch].
 */
public interface SearcherForHits<T : CommonSearchParameters> : SearcherQuery<T, ResponseSearch>
