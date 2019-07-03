package com.algolia.instantsearch.helper.hierarchical

import com.algolia.search.model.search.Facet


public data class HierarchicalItem(
    val facet: Facet,
    val displayName: String,
    val level: Int
)