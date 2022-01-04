package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.helper.searcher.util.SearcherForHits
import com.algolia.search.client.Index
import com.algolia.search.model.params.CommonSearchParameters

/**
 * Single index searcher.
 */
public interface SearcherIndex<T : CommonSearchParameters> : SearcherForHits<T> {

    /**
     * Searcher's Index.
     */
    public var index: Index
}
