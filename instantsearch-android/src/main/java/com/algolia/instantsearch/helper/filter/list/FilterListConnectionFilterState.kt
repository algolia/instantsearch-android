package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.instantsearch.helper.filter.state.add
import com.algolia.search.model.filter.Filter

internal data class FilterListConnectionFilterState<T : Filter>(
    private val viewModel: FilterListViewModel<T>,
    private val filterState: FilterState,
    private val groupID: FilterGroupID,
    private val getSelections: Filters.() -> Set<T>,
) : ConnectionImpl() {

    private val updateSelections: Callback<Filters> = { filters ->
        viewModel.selections.value = filters.getSelections()
    }
    private val updateFilterState: Callback<Set<Filter>> = { selections ->
        filterState.notify {
            when (viewModel.selectionMode) {
                SelectionMode.Single -> clear(groupID)
                SelectionMode.Multiple -> viewModel.items.value.forEach { remove(groupID, it) }
            }
            add(groupID, selections)
        }
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
