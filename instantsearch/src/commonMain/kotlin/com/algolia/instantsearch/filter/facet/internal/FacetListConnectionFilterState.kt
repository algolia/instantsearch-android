package com.algolia.instantsearch.filter.facet.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.filter.facet.FacetListViewModel
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.Filters
import com.algolia.instantsearch.filter.state.add
import com.algolia.instantsearch.filter.state.getValue
import com.algolia.instantsearch.filter.state.remove

import com.algolia.instantsearch.filter.Filter
import kotlinx.serialization.InternalSerializationApi

internal data class FacetListConnectionFilterState(
    private val viewModel: FacetListViewModel,
    private val filterState: FilterState,
    private val attribute: String,
    private val groupID: FilterGroupID,
) : AbstractConnection() {

    private val updateSelections: Callback<Filters> = { filters ->
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

    @OptIn(InternalSerializationApi::class)
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
