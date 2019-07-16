package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


public fun HierarchicalViewModel.connectFilterState(
    filterState: FilterState,
    attribute: Attribute,
    filterGroupName: String = attribute.raw
) {
    // hierarchical facet's FilterOperator MUST be And
    val filterGroupID = FilterGroupID(filterGroupName, FilterOperator.And)

    filterState.hierarchicalAttributes = hierarchicalAttributes
    onSelectionsComputed += { selections ->
        filterState.notify {
            val last = selections.last()
            val filter = Filter.Facet(last.first, last.second)
            val filters = selections.map { Filter.Facet(it.first, it.second) }

            filterState.clear(filterGroupID)
            filterState.add(filterGroupID, filter)
            filterState.hierarchicalFilters = filters
        }
    }
    filterState.onChanged += {
        selections = (it.getFacetFilters(filterGroupID).first().value as Filter.Facet.Value.String).raw.split(separator)
    }
}