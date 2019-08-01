package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter


public data class FilterCurrentConnector(
    public val filterState: FilterState,
    public val groupIDs: List<FilterGroupID> = listOf(),
    public val viewModel: FilterCurrentViewModel = FilterCurrentViewModel()
) : ConnectionImpl() {

    public constructor(
        filters: Map<FilterAndID, Filter>,
        filterState: FilterState,
        groupIDs: List<FilterGroupID> = listOf()
    ) : this(
        filterState,
        groupIDs,
        FilterCurrentViewModel(filters)
    )

    private val connectionFilterState = viewModel.connectFilterState(filterState, groupIDs)

    override fun connect() {
        super.connect()
        connectionFilterState.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionFilterState.disconnect()
    }
}