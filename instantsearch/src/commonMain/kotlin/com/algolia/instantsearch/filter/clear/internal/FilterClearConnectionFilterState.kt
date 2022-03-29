package com.algolia.instantsearch.filter.clear.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.filter.clear.ClearMode
import com.algolia.instantsearch.filter.clear.FilterClearViewModel
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState

internal data class FilterClearConnectionFilterState(
    private val viewModel: FilterClearViewModel,
    private val filterState: FilterState,
    private val groupIDs: List<FilterGroupID>,
    private val mode: ClearMode,
) : ConnectionImpl() {

    private val updateFilterState: Callback<Unit> = {
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
