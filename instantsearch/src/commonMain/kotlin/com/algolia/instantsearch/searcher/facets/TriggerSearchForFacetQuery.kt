package com.algolia.instantsearch.searcher.facets

import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Query

public fun interface TriggerSearchForFacetQuery {

    public fun triggerFor(query: Query, attribute: Attribute, facetQuery: String?): Boolean
}
