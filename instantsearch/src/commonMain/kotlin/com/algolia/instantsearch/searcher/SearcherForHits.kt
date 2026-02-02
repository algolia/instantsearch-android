package com.algolia.instantsearch.searcher

import com.algolia.client.model.search.SearchParams
import com.algolia.client.model.search.SearchResponse


/**
 * Implementation of [SearcherQuery] with [SearchParams] and [SearchResponse].
 */
public interface SearcherForHits<T : SearchParams> : SearcherQuery<T, SearchResponse>
