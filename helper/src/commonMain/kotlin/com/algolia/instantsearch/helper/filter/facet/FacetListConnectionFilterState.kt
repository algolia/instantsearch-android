package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.event.Callback
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.state.*
import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter


internal class FacetListConnectionFilterState(
    private val viewModel: FacetListViewModel,
    private val filterState: FilterState,
    private val attribute: Attribute,
    private val groupID: FilterGroupID
) : ConnectionImpl() {

    private val updateSelections: Callback<MutableFilters> = { filters ->
        viewModel.selections.value = filters.getFacetFilters(groupID).map { it.getValue() }.toSet()
    }
    private val updateFilterState: Callback<Set<String>> = { selections ->
        val filters = selections.map { Filter.Facet(attribute, it) }.toSet()

        filterState.notify {
            when (viewModel.selectionMode) {
                SelectionMode.Single -> clear(groupID)
                SelectionMode.Multiple -> remove(groupID, viewModel.getFiltersToRemove())
            }
            add(groupID, filters)
        }
    }

    private fun FacetListViewModel.getFiltersToRemove(): Set<Filter.Facet> {
        val currentFilters = items.value.map { Filter.Facet(attribute, it.value) }.toSet()
        val currentSelections = selections.value.map { Filter.Facet(attribute, it) }

        return if (persistentSelection) currentFilters + currentSelections else currentFilters
    }

    override fun connect() {
        super.connect()
        filterState.filters.subscribePast(updateSelections)
        viewModel.eventSelection.subscribe(updateFilterState)
    }

    override fun disconnect() {
        super.disconnect()
        filterState.filters.unsubscribe(updateSelections)
        viewModel.eventSelection.unsubscribe(updateFilterState)
    }
}