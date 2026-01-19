package com.algolia.instantsearch.searcher

import com.algolia.client.model.search.SearchParamsObject

public fun SearchParamsObject.addFacet(vararg attribute: String) {
    facets = facets.orEmpty().toMutableSet().also {
        it += attribute
    }
}

public fun SearchParamsObject.removeFacet(attribute: String) {
    facets = facets.orEmpty().toMutableSet().also {
        it -= attribute
    }
}
