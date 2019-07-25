package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public class FilterClearWidget(
    public val filterState: FilterState,
    public val groupIDs: List<FilterGroupID> = listOf(),
    public val mode: ClearMode = ClearMode.Specified,
    public val viewModel: FilterClearViewModel = FilterClearViewModel()
) : ConnectionImpl() {

    private val connectionFilterState = viewModel.connectionFilterState(filterState, groupIDs, mode)

    override fun connect() {
        super.connect()
        connectionFilterState.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionFilterState.disconnect()
    }
}