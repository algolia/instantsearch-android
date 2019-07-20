package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


internal class FilterClearConnectionFilterState(
    private val viewModel: FilterClearViewModel,
    private val filterState: FilterState,
    private val groupIDs: List<FilterGroupID>,
    private val mode: ClearMode
) : ConnectionImpl() {

    private val updateFilterState: (Unit) -> Unit = {
        filterState.notify {
            when (mode) {
                ClearMode.Specified -> clear(*groupIDs.toTypedArray())
                ClearMode.Except -> clearExcept(groupIDs)
            }
        }
    }

    override fun connect() {
        super.connect()
        viewModel.eventClear.subscribe(updateFilterState)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.eventClear.unsubscribe(updateFilterState)
    }
}