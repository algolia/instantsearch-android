package com.algolia.instantsearch.helper.filter.map

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter

/**
 * Components holding a map of filters, and that can apply a single filter at a time.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/filter-map/android/)
 *
 * @param filterState the current state of the filters
 * @param viewModel the logic applied to the filter map
 * @param groupID groups all created filters under an ID
 */
public data class FilterMapConnector(
    public val filterState: FilterState,
    public val viewModel: FilterMapViewModel = FilterMapViewModel(),
    public val groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
) : ConnectionImpl() {

    /**
     * @param filters the map of filters to be held. The key is an unique identifier for the filter value
     * @param filterState the current state of the filters
     * @param selected the key of the filter selected by default
     * @param groupID groups all created filters under an ID
     */
    public constructor(
        filters: Map<Int, Filter>,
        filterState: FilterState,
        selected: Int? = null,
        groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
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
