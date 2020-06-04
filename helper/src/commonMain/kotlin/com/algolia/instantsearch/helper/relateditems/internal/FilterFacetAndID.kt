package com.algolia.instantsearch.helper.relateditems.internal

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.search.model.filter.Filter

internal class FilterFacetAndID(
    val filterGroupID: FilterGroupID,
    val filterFacets: Array<Filter.Facet>
)
