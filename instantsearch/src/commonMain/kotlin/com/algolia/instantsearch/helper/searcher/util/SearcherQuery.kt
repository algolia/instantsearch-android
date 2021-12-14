package com.algolia.instantsearch.helper.searcher.util

import com.algolia.instantsearch.helper.searcher.SearcherQuery
import com.algolia.search.model.Attribute
import com.algolia.search.model.params.CommonSearchParameters
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearchForFacets
import com.algolia.search.model.search.Query

/**
 * Convenience implementation of [SearcherQuery] with [Query] and [ResponseSearch].
 */
public typealias SearcherForHits<T> = SearcherQuery<T, ResponseSearch>

/**
 * Implementation of [SearcherQuery] with [Query] and [ResponseSearchForFacets].
 */
public interface SearcherForFacets<T : CommonSearchParameters> : SearcherQuery<T, ResponseSearchForFacets> {

    /**
     * Facets attribute.
     */
    public val attribute: Attribute
}
