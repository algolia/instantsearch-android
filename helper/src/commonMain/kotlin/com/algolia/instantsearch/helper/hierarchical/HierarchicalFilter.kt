package com.algolia.instantsearch.helper.hierarchical

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import kotlin.jvm.JvmField


public data class HierarchicalFilter(
    @JvmField
    val attributes: List<Attribute>,
    @JvmField
    val path: List<Filter.Facet>,
    @JvmField
    val filter: Filter.Facet
)