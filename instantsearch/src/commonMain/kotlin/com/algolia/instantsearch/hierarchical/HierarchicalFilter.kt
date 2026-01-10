package com.algolia.instantsearch.hierarchical

import com.algolia.instantsearch.migration2to3.Attribute
import com.algolia.instantsearch.migration2to3.Filter


public data class HierarchicalFilter(
    val attributes: List<Attribute>,
    val path: List<Filter.Facet>,
    val filter: Filter.Facet,
)
