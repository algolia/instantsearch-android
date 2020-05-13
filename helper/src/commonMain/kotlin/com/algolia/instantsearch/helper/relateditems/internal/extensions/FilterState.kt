package com.algolia.instantsearch.helper.relateditems.internal.extensions

import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.relateditems.MatchingPattern
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.filter.FilterGroup

internal fun <T> FilterState.addMatchingPattern(hit: T, matchingPattern: MatchingPattern<T>) {
    val optionalFilter = matchingPattern.toOptionalFilter(hit)
    add(optionalFilter.filterGroupID, *optionalFilter.filterFacets)
}

internal fun FilterState.toFilterFacetGroup(): Set<FilterGroup<Filter.Facet>> {
    return getFacetGroups().map { (key, value) ->
        when (key.operator) {
            FilterOperator.And -> FilterGroup.And.Facet(value, key.name)
            FilterOperator.Or -> FilterGroup.Or.Facet(value, key.name)
        }
    }.toSet()
}
