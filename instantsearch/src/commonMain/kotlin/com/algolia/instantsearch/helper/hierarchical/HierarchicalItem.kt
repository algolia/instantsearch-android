package com.algolia.instantsearch.helper.hierarchical

import com.algolia.search.model.search.Facet

public data class HierarchicalItem(
    val facet: Facet,
    val displayName: String,
    val level: Int,
    val isSelected: Boolean
) {

    public constructor(
        facet: Facet,
        displayName: String,
        level: Int
    ) : this(facet = facet, displayName = displayName, level = level, isSelected = false)
}
