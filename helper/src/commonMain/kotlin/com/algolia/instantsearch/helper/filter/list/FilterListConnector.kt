package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.filter.Filter


public abstract class FilterListConnector<T : Filter> : ConnectionImpl() {

    public abstract val filterState: FilterState
    public abstract val viewModel: FilterListViewModel<T>
    public abstract val groupID: FilterGroupID
    protected abstract val connectionFilterState: Connection

    override fun connect() {
        super.connect()
        connectionFilterState.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionFilterState.disconnect()
    }

    data class Facet(
        override val filterState: FilterState,
        override val viewModel: FilterListViewModel.Facet = FilterListViewModel.Facet(),
        override val groupID: FilterGroupID = FilterGroupID(FilterOperator.Or)
    ) : FilterListConnector<Filter.Facet>() {

        override val connectionFilterState = viewModel.connectFilterState(filterState, groupID)

        constructor(
            filters: List<Filter.Facet>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Multiple,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.Or)
        ) : this(filterState, FilterListViewModel.Facet(filters, selectionMode), groupID)
    }

    data class Numeric(
        override val filterState: FilterState,
        override val viewModel: FilterListViewModel.Numeric = FilterListViewModel.Numeric(),
        override val groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
    ) : FilterListConnector<Filter.Numeric>() {

        override val connectionFilterState = viewModel.connectFilterState(filterState, groupID)

        constructor(
            filters: List<Filter.Numeric>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Single,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
        ) : this(filterState, FilterListViewModel.Numeric(filters, selectionMode), groupID)
    }

    data class Tag(
        override val filterState: FilterState,
        override val viewModel: FilterListViewModel.Tag = FilterListViewModel.Tag(),
        override val groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
    ) : FilterListConnector<Filter.Tag>() {

        override val connectionFilterState = viewModel.connectFilterState(filterState, groupID)

        constructor(
            filters: List<Filter.Tag>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Multiple,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
        ) : this(filterState, FilterListViewModel.Tag(filters, selectionMode), groupID)
    }

    data class All(
        override val filterState: FilterState,
        override val viewModel: FilterListViewModel.All = FilterListViewModel.All(),
        override val groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
    ) : FilterListConnector<Filter>() {

        override val connectionFilterState = viewModel.connectFilterState(filterState, groupID)

        constructor(
            filters: List<Filter>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Multiple,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.And)
        ) : this(filterState, FilterListViewModel.All(filters, selectionMode), groupID)
    }
}