package com.algolia.instantsearch.hierarchical

import com.algolia.client.model.search.FacetHits

public data class HierarchicalItem(
    val facet: FacetHits,
    val displayName: String,
    val level: Int,
    val isSelected: Boolean,
) {

    public constructor(
        facet: FacetHits,
        displayName: String,
        level: Int,
    ) : this(facet = facet, displayName = displayName, level = level, isSelected = false)
}
