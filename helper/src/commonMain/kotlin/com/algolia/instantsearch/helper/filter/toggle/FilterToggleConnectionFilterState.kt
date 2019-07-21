package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.event.Callback
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.filter.Filter


internal class FilterToggleConnectionFilterState(
    private val viewModel: FilterToggleViewModel,
    private val filterState: FilterState,
    private val default: Filter?,
    private val groupID: FilterGroupID
) : ConnectionImpl() {

    init {
        if (default != null) filterState.add(groupID, default)
    }

    private val updateIsSelected: Callback<Filters> = { filters ->
        viewModel.isSelected.value = filters.contains(groupID, viewModel.item.value)
    }
    private val updateFilterState: Callback<Boolean> = { isSelected ->
        filterState.notify {
            if (isSelected) {
                if (default != null) remove(groupID, default)
                add(groupID, viewModel.item.value)
            } else {
                remove(groupID, viewModel.item.value)
                if (default != null) add(groupID, default)
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