package com.algolia.instantsearch.filter.toggle.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.Filters
import com.algolia.instantsearch.filter.toggle.FilterToggleViewModel

internal data class FilterToggleConnectionFilterState(
    private val viewModel: FilterToggleViewModel,
    private val filterState: FilterState,
    private val groupID: FilterGroupID,
) : AbstractConnection() {

    private val updateIsSelected: Callback<Filters> = { filters ->
        viewModel.isSelected.value = filters.contains(groupID, viewModel.item.value)
    }
    private val updateFilterState: Callback<Boolean> = { isSelected ->
        filterState.notify {
            if (isSelected) {
                add(groupID, viewModel.item.value)
            } else {
                remove(groupID, viewModel.item.value)
            }
        }
    }

    override fun connect() {
        super.connect()
        filterState.filters.subscribePast(updateIsSelected)
        viewModel.eventSelection.subscribe(updateFilterState)
    }

    override fun disconnect() {
        super.disconnect()
        filterState.filters.unsubscribe(updateIsSelected)
        viewModel.eventSelection.unsubscribe(updateFilterState)
    }
}
