package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter

/**
 * Filter Toggle is a filtering view that displays any kind of filter, and lets the user refine the search results
 * by toggling it on or off.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/toggle-refinement/android/)
 *
 * @param filterState the current state of the filters
 * @param viewModel the logic applied to the filter
 * @param groupID the filter will be grouped under this ID and will be composed with this operator
 */
public data class FilterToggleConnector(
    public val filterState: FilterState,
    public val viewModel: FilterToggleViewModel,
    public val groupID: FilterGroupID = FilterGroupID(viewModel.item.value.attribute, FilterOperator.And),
) : ConnectionImpl() {

    /**
     * @param filterState the current state of the filters
     * @param filter the filter to apply.
     * @param isSelected If true, the filter will be active when created
     * @param groupID the filter will be grouped under this ID and will be composed with this operator
     */
    public constructor(
        filterState: FilterState,
        filter: Filter,
        isSelected: Boolean = false,
        groupID: FilterGroupID = FilterGroupID(filter.attribute, FilterOperator.And),
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
