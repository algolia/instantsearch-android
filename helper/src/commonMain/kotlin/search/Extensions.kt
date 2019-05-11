package search

import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Query


fun Query.addFacet(attribute: Attribute) {
    facets = facets.orEmpty().toMutableSet().also {
        it += attribute
    }
}