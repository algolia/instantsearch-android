package com.algolia.instantsearch.helper.searcher

import com.algolia.search.model.Attribute
import com.algolia.search.model.params.CommonSearchParameters
import com.algolia.search.model.response.ResponseSearchForFacets

/**
 * Implementation of [SearcherQuery] with [CommonSearchParameters] and [ResponseSearchForFacets].
 */
public interface SearcherForFacets<T : CommonSearchParameters> : SearcherQuery<T, ResponseSearchForFacets> {

    /**
     * Facets attribute.
     */
    public val attribute: Attribute
}
