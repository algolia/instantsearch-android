package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.search.client.Index
import com.algolia.search.model.params.CommonSearchParameters
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.transport.RequestOptions

/**
 * Single index searcher.
 */
public interface SearcherIndex<T : CommonSearchParameters> : Searcher<ResponseSearch>, QueryHolder<T> {

    /**
     * Searcher's Index.
     */
    public var index: Index

    /**
     * Additional/Custom request options.
     */
    public val requestOptions: RequestOptions?
}
