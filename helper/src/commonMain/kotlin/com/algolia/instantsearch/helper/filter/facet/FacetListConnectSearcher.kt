package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.search.model.Attribute
import com.algolia.search.model.response.ResponseSearch
import com.algolia.search.model.response.ResponseSearchForFacets


public fun FacetListViewModel.connectSearcher(
    attribute: Attribute,
    searcher: SearcherSingleIndex
) {
    val onResponseChanged = { response: ResponseSearch ->
        val disjunctiveFacets = response.disjunctiveFacetsOrNull?.get(attribute)

        item = disjunctiveFacets ?: response.facetsOrNull.orEmpty()[attribute].orEmpty()
    }

    searcher.query.addFacet(attribute)
    searcher.response?.let(onResponseChanged)
    searcher.onResponseChanged += onResponseChanged
}

public fun FacetListViewModel.connectSearcherForFacet(
    searcher: SearcherForFacets
) {
    val onResponseChanged = { response: ResponseSearchForFacets ->
        item = response.facets
    }

    searcher.response?.let(onResponseChanged)
    searcher.onResponseChanged += onResponseChanged
}