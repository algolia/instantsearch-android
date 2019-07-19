package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.helper.searcher.SearcherForFacets
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet
import com.algolia.search.model.Attribute


public fun FacetListViewModel.connectSearcher(
    attribute: Attribute,
    searcher: SearcherSingleIndex
) {
    searcher.query.addFacet(attribute)
    searcher.response.subscribePast { response ->
        if (response != null) {
            val disjunctiveFacets = response.disjunctiveFacetsOrNull?.get(attribute)

            items.value = disjunctiveFacets ?: response.facetsOrNull.orEmpty()[attribute].orEmpty()
        }
    }
}

public fun FacetListViewModel.connectSearcherForFacet(
    searcher: SearcherForFacets
) {
    searcher.response.subscribePast { response ->
        if (response != null) {
            items.value = response.facets
        }
    }
}