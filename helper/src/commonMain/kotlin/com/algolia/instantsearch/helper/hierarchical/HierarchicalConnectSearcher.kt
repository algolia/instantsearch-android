package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.addFacet


public fun HierarchicalViewModel.connectSearcher(searcher: SearcherSingleIndex) {
    searcher.query.addFacet(*hierarchicalAttributes.toTypedArray())
    searcher.onResponseChanged += { response ->
        val facets = response.hierarchicalFacetsOrNull ?:
        response.facets.filter { it.key == hierarchicalAttributes.first() }

        item = hierarchicalAttributes.mapNotNull { facets[it] }.flatten().toNodes()
    }
}