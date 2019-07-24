package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.connect
import com.algolia.instantsearch.helper.connection.ConnectionImplWidget
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState


public class FilterClearWidget(
    public val filterState: FilterState,
    public val groupIDs: List<FilterGroupID> = listOf(),
    public val mode: ClearMode = ClearMode.Specified,
    public val viewModel: FilterClearViewModel = FilterClearViewModel()
) : ConnectionImplWidget() {

    override val connections = listOf(viewModel.connectionFilterState(filterState, groupIDs, mode)).connect()

    public fun with(vararg views: FilterClearView): List<Connection> {
        return views.map(viewModel::connectionView).connect()
    }
}