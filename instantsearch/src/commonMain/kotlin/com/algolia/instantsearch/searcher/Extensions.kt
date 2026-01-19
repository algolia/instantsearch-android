package com.algolia.instantsearch.searcher

import com.algolia.search.model.Attribute
import com.algolia.search.model.params.CommonSearchParameters

public fun CommonSearchParameters.addFacet(vararg attribute: String) {
    facets = facets.orEmpty().toMutableSet().also {
        it += attribute
    }
}

public fun CommonSearchParameters.removeFacet(attribute: String) {
    facets = facets.orEmpty().toMutableSet().also {
        it -= attribute
    }
}
