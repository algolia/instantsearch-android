package com.algolia.instantsearch.searcher

import com.algolia.search.model.IndexName

/**
 * Component holding [IndexName].
 */
public interface IndexNameHolder {

    /**
     * Index name for search operations
     */
    public var indexName: IndexName
}
