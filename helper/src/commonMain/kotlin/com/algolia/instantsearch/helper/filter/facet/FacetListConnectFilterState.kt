package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.helper.filter.state.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


public fun FacetListViewModel.connectFilterState(
    attribute: Attribute,
    filterState: FilterState,
    groupID: FilterGroupID = FilterGroupID.Or(attribute)
) {
    onSelectionsComputed += { selections ->
        val filters = selections.map { Filter.Facet(attribute, it) }.toSet()

        filterState.notify {
            clear(groupID)
            add(groupID, filters)
        }
    }
    val onChange: (Filters) -> Unit = { filters ->
        selections = filters.getFacetFilters(groupID)
            .map { it.getValue() }
            .toSet()
    }

    onChange(filterState)
    filterState.onChange += onChange
}