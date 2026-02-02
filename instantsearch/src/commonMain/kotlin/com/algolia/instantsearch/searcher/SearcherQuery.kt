package com.algolia.instantsearch.searcher

import com.algolia.client.model.search.SearchParams
import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.core.searcher.Searcher

/**
 * Searcher with request capabilities.
 */
public interface SearcherQuery<T : SearchParams, R> : Searcher<R> {

    /**
     * Query for search operations.
     */
    public var query: T

    /**
     * Additional/Custom request options.
     */
    public val requestOptions: RequestOptions?
}
