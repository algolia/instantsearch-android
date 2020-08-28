package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter

/**
 * Current Refinements shows the currently active refinements within a given FilterState and lets users remove filters
 * individually.
 * [Documentation](https://www.algolia.com/doc/api-reference/widgets/current-refinements/android/)
 *
 * @param filterState the default filters to display
 * @param groupIDs the FilterState that will hold your filters
 * @param viewModel the logic for current refinements in the FilterState
 */
public data class FilterCurrentConnector(
    public val filterState: FilterState,
    public val groupIDs: List<FilterGroupID> = listOf(),
    public val viewModel: FilterCurrentViewModel = FilterCurrentViewModel(),
) : ConnectionImpl() {

    /**
     * @param filters the default filters to display
     * @param filterState the FilterState that will hold your filters
     * @param groupIDs when specified, only matching current refinements will be displayed.
     */
    public constructor(
        filters: Map<FilterAndID, Filter>,
        filterState: FilterState,
        groupIDs: List<FilterGroupID> = listOf(),
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
