package com.algolia.instantsearch.searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.search.model.params.CommonSearchParameters
import com.algolia.search.transport.RequestOptions

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
