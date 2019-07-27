package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters


internal class FilterToggleConnectionFilterState(
    private val viewModel: FilterToggleViewModel,
    private val filterState: FilterState,
    private val groupID: FilterGroupID
) : ConnectionImpl() {

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