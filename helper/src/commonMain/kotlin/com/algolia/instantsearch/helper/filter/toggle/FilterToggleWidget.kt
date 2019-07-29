package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter


public class FilterToggleWidget(
    public val filterState: FilterState,
    public val viewModel: FilterToggleViewModel,
    public val groupID: FilterGroupID = FilterGroupID(viewModel.item.value.attribute, FilterOperator.And)
) : ConnectionImpl() {

    public constructor(
        filterState: FilterState,
        filter: Filter,
        isSelected: Boolean = false,
        groupID: FilterGroupID = FilterGroupID(filter.attribute, FilterOperator.And)
    ) : this(filterState, FilterToggleViewModel(filter, isSelected), groupID)

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