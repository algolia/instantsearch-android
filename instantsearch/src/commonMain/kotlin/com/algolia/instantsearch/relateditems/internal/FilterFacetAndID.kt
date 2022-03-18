package com.algolia.instantsearch.relateditems.internal

import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.search.model.filter.Filter

internal class FilterFacetAndID(
    val filterGroupID: FilterGroupID,
    val filterFacets: Array<Filter.Facet>,
)
