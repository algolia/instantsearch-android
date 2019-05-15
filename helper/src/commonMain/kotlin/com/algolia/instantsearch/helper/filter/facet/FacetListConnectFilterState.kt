package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.selectable.list.SelectionMode
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
        val current = items.map { Filter.Facet(attribute, it.value) }.toSet()

        filterState.notify {
            when (selectionMode) {
                SelectionMode.Single -> clear(groupID)
                SelectionMode.Multiple -> remove(groupID, current)
            }
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