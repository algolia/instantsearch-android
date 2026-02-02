package com.algolia.instantsearch.hierarchical

import com.algolia.instantsearch.filter.Filter


public data class HierarchicalFilter(
    val attributes: List<String>,
    val path: List<Filter.Facet>,
    val filter: Filter.Facet,
)
