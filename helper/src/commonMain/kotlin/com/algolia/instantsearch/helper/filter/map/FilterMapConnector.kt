package com.algolia.instantsearch.helper.filter.map

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter
import kotlin.jvm.JvmOverloads


public data class FilterMapConnector @JvmOverloads constructor(
    public val filterState: FilterState,
    public val viewModel: FilterMapViewModel = FilterMapViewModel(),
    public val groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
) : ConnectionImpl() {

    public constructor(
        filters: Map<Int, Filter>,
        filterState: FilterState,
        selected: Int? = null,
        groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
    ) : this(filterState, FilterMapViewModel(filters, selected), groupID)

    private val connectionFilterState = viewModel.connectFilterState(filterState, groupID)

    override fun connect() {
        super.connect()
        connectionFilterState.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionFilterState.disconnect()
    }
}