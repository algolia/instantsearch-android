package com.algolia.instantsearch.searcher

import com.algolia.client.transport.RequestOptions
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.migration2to3.CommonSearchParameters

/**
 * Searcher with request capabilities.
 */
public interface SearcherQuery<out T : CommonSearchParameters, R> : Searcher<R> {

    /**
     * Query for search operations.
     */
    public val query: T

    /**
     * Additional/Custom request options.
     */
    public val requestOptions: RequestOptions?
}
