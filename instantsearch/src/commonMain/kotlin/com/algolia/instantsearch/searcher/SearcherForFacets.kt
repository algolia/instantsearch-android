package com.algolia.instantsearch.searcher

import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.CommonSearchParameters
import com.algolia.instantsearch.migration2to3.ResponseSearchForFacets


/**
 * Implementation of [SearcherQuery] with [CommonSearchParameters] and [ResponseSearchForFacets].
 */
public interface SearcherForFacets<T : CommonSearchParameters> : SearcherQuery<T, ResponseSearchForFacets> {

    /**
     * Facets attribute.
     */
    public val attribute: Attribute
}
