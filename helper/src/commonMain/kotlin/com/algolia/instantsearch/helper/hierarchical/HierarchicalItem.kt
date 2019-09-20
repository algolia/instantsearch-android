package com.algolia.instantsearch.helper.hierarchical

import com.algolia.search.model.search.Facet
import kotlin.jvm.JvmField


public data class HierarchicalItem(
    @JvmField
    val facet: Facet,
    @JvmField
    val displayName: String,
    @JvmField
    val level: Int
)