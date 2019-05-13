package com.algolia.instantsearch.helper.searcher

import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Query


fun Query.addFacet(attribute: Attribute) {
    facets = facets.orEmpty().toMutableSet().also {
        it += attribute
    }
}

fun FilterState.connect(searcher: Searcher): FilterState {
    onChange += {
        searcher.search(it)
    }
    return this
}