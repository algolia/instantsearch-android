package com.algolia.instantsearch.helper.searcher

import com.algolia.search.client.Index
import com.algolia.search.model.params.CommonSearchParameters
import com.algolia.search.model.response.ResponseSearch

/**
 * Single index searcher.
 */
public interface SearcherIndex<T : CommonSearchParameters> : SearcherQuery<T, ResponseSearch> {

    /**
     * Searcher's Index.
     */
    public var index: Index
}
