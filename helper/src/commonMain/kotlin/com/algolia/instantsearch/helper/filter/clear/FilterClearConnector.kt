package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState

/**
 * A Connector to setup a FilterClear widget's business logic,
 * linking its [viewModel] to a [filterState] according to a [mode] and some [groupIDs].
 */
public data class FilterClearConnector(
    public val filterState: FilterState,
    public val groupIDs: List<FilterGroupID> = listOf(),
    public val mode: ClearMode = ClearMode.Specified,
    public val viewModel: FilterClearViewModel = FilterClearViewModel()
) : ConnectionImpl() {

    private val connectionFilterState = viewModel.connectFilterState(filterState, groupIDs, mode)

    override fun connect() {
        super.connect()
        connectionFilterState.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionFilterState.disconnect()
    }
}