package com.algolia.instantsearch.helper.searcher

import com.algolia.search.model.Attribute
import com.algolia.search.model.params.CommonSearchParameters

public fun CommonSearchParameters.addFacet(vararg attribute: Attribute) {
    facets = facets.orEmpty().toMutableSet().also {
        it += attribute
    }
}

public fun CommonSearchParameters.removeFacet(attribute: Attribute) {
    facets = facets.orEmpty().toMutableSet().also {
        it -= attribute
    }
}
