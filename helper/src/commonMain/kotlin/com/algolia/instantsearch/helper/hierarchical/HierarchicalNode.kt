package com.algolia.instantsearch.helper.hierarchical

import com.algolia.search.model.search.Facet


public data class HierarchicalNode(
    val facet: Facet,
    val children: MutableList<HierarchicalNode> = mutableListOf()
)