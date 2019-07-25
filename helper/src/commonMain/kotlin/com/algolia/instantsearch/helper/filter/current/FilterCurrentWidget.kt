package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.connect
import com.algolia.instantsearch.helper.connection.ConnectionImplWidget
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter


public class FilterCurrentWidget(
    public val filterState: FilterState,
    public val groupIDs: List<FilterGroupID> = listOf(),
    public val viewModel: FilterCurrentViewModel = FilterCurrentViewModel()
) : ConnectionImplWidget() {

    public constructor(
        filters: Map<FilterAndID, Filter>,
        filterState: FilterState,
        groupIDs: List<FilterGroupID> = listOf()
    ) : this(
        filterState,
        groupIDs,
        FilterCurrentViewModel(filters)
    )

    override val connections = listOf(viewModel.connectionFilterState(filterState, groupIDs)).connect()

    public fun with(
        vararg views: FilterCurrentView,
        presenter: FilterCurrentPresenter = FilterCurrentPresenterImpl()
    ): List<Connection> {
        return views.map { viewModel.connectionView(it, presenter) }.connect()
    }
}