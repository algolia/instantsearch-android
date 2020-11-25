package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.search.client.Index
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.transport.RequestOptions

/**
 * Single index searcher.
 */
public interface SearcherIndex<T> : Searcher<ResponseSearch> {

    /**
     * Searcher's Index.
     */
    public var index: Index

    /**
     * Query to run.
     */
    public val query: T

    /**
     * Additional/Custom request options.
     */
    public val requestOptions: RequestOptions?
}
