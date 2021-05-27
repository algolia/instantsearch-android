package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState

/**
 * ClearFilters lets the user clear all refinements that are currently active within the given FilterState.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/clear-refinements/android/)
 *
 * @param filterState the FilterState that will hold your filters
 * @param groupIDs the groupIDs of filters to clear. All filters will be cleared if unspecified
 * @param mode whether we should clear the Specified filters or all filters Except them
 * @param viewModel the view that will render the clear filter UI.
 */
public data class FilterClearConnector(
    public val filterState: FilterState,
    public val groupIDs: List<FilterGroupID> = listOf(),
    public val mode: ClearMode = ClearMode.Specified,
    public val viewModel: FilterClearViewModel = FilterClearViewModel(),
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
