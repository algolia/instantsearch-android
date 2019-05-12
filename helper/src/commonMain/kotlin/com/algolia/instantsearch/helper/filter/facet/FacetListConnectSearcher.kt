package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.helper.searcher.SearcherForFacet
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.search.model.Attribute


public fun FacetListViewModel.connectSearcher(
    attribute: Attribute,
    searcher: SearcherSingleIndex
) {
    searcher.query.addFacet(attribute)
    searcher.onResponseChanged += { response ->
        val disjunctiveFacets = response.disjunctiveFacetsOrNull?.get(attribute)

        items = disjunctiveFacets ?: response.facetsOrNull.orEmpty()[attribute].orEmpty()
    }
}

public fun FacetListViewModel.connectSearcherFacet(
    searcher: SearcherForFacet
) {
    searcher.onResponseChanged += { response ->
        items = response.facets
    }
}