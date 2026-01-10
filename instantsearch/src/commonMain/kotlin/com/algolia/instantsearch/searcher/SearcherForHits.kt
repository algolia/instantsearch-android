package com.algolia.instantsearch.searcher

import com.algolia.instantsearch.migration2to3.CommonSearchParameters
import com.algolia.instantsearch.migration2to3.ResponseSearch


/**
 * Implementation of [SearcherQuery] with [CommonSearchParameters] and [ResponseSearch].
 */
public interface SearcherForHits<T : CommonSearchParameters> : SearcherQuery<T, ResponseSearch>
