package com.algolia.instantsearch.helper.searcher

import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Query


public fun Query.addFacet(attribute: Attribute) {
    facets = facets.orEmpty().toMutableSet().also {
        it += attribute
    }
}