package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter


public abstract class FilterListWidget<T : Filter> internal constructor(
    public val filterState: FilterState,
    public val viewModel: FilterListViewModel<T>,
    public val groupID: FilterGroupID,
    private val connectionFilterState: Connection
) : ConnectionImpl() {

    override fun connect() {
        super.connect()
        connectionFilterState.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionFilterState.disconnect()
    }

    class Facet(
        filterState: FilterState,
        viewModel: FilterListViewModel.Facet = FilterListViewModel.Facet(),
        groupID: FilterGroupID = FilterGroupID(FilterOperator.Or)
    ) : FilterListWidget<Filter.Facet>(
        filterState,
        viewModel,
        groupID,
        viewModel.connectFilterState(filterState, groupID)
    ) {

        constructor(
            items: List<Filter.Facet>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Multiple,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.Or)
        ) : this(filterState, FilterListViewModel.Facet(items, selectionMode), groupID)
    }

    class Numeric(
        filterState: FilterState,
        viewModel: FilterListViewModel.Numeric = FilterListViewModel.Numeric(),
        groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
    ) : FilterListWidget<Filter.Numeric>(
        filterState,
        viewModel,
        groupID,
        viewModel.connectFilterState(filterState, groupID)
    ) {

        constructor(
            items: List<Filter.Numeric>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Single,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
        ) : this(filterState, FilterListViewModel.Numeric(items, selectionMode), groupID)
    }

    class Tag(
        filterState: FilterState,
        viewModel: FilterListViewModel.Tag = FilterListViewModel.Tag(),
        groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
    ) : FilterListWidget<Filter.Tag>(
        filterState,
        viewModel,
        groupID,
        viewModel.connectFilterState(filterState, groupID)
    ) {

        constructor(
            items: List<Filter.Tag>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Multiple,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
        ) : this(filterState, FilterListViewModel.Tag(items, selectionMode), groupID)
    }

    class All(
        filterState: FilterState,
        viewModel: FilterListViewModel.All = FilterListViewModel.All(),
        groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
    ) : FilterListWidget<Filter>(
        filterState,
        viewModel,
        groupID,
        viewModel.connectFilterState(filterState, groupID)
    ) {

        constructor(
            items: List<Filter>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Multiple,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
        ) : this(filterState, FilterListViewModel.All(items, selectionMode), groupID)
    }
}