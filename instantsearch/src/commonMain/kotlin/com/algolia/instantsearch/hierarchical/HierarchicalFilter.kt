package com.algolia.instantsearch.hierarchical

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter

public data class HierarchicalFilter(
    val attributes: List<Attribute>,
    val path: List<Filter.Facet>,
    val filter: Filter.Facet,
)
