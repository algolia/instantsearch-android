package com.algolia.instantsearch.searcher

import com.algolia.instantsearch.migration2to3.IndexName

/**
 * Component holding [IndexName].
 */
public interface IndexNameHolder {

    /**
     * Index name for search operations
     */
    public var indexName: IndexName
}
