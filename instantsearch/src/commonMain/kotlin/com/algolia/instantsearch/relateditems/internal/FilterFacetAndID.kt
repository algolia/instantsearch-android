package com.algolia.instantsearch.relateditems.internal

import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.migration2to3.Filter

internal class FilterFacetAndID(
    val filterGroupID: FilterGroupID,
    val filterFacets: Array<Filter.Facet>,
)
