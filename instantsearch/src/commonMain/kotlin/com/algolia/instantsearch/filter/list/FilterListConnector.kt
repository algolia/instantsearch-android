package com.algolia.instantsearch.filter.list

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.AbstractConnection
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.extension.traceFacetFilterListConnector
import com.algolia.instantsearch.extension.traceFilterListConnector
import com.algolia.instantsearch.extension.traceNumericFilterListConnector
import com.algolia.instantsearch.extension.traceTagFilterListConnector
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterOperator
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.Filter

/**
 * Filtering view that displays any kind of tag filters and lets the user refine the search results by selecting them.
 * Compared to the RefinementList, which takes its values from the search response facets, this widget displays filters
 * that you add yourself.
 */
public abstract class FilterListConnector<T : Filter> : AbstractConnection() {

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

    /**
     * [Documentation](https://www.algolia.com/doc/api-reference/widgets/filter-list-facet/android/)
     *
     * @param filterState The FilterState that will hold your filters.
     * @param viewModel The logic applied to the tag filters.
     * @param groupID The identifier of the group of filters.
     */
    public data class Facet(
        override val filterState: FilterState,
        override val viewModel: FilterListViewModel.Facet = FilterListViewModel.Facet(),
        override val groupID: FilterGroupID = FilterGroupID(FilterOperator.Or),
    ) : FilterListConnector<Filter.Facet>() {

        override val connectionFilterState: Connection = viewModel.connectFilterState(filterState, groupID)

        /**
         * @param filters The logic applied to the tag filters.
         * @param filterState The FilterState that will hold your filters.
         * @param selectionMode Whether the list can have Single or Multiple selections.
         * @param groupID The identifier of the group of filters.
         */
        public constructor(
            filters: List<Filter.Facet>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Multiple,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.Or),
        ) : this(filterState, FilterListViewModel.Facet(filters, selectionMode), groupID)

        init {
            traceFacetFilterListConnector()
        }
    }

    /**
     * [Documentation](https://www.algolia.com/doc/api-reference/widgets/filter-list-numeric/android/)
     *
     * @param filterState The FilterState that will hold your filters.
     * @param viewModel The logic applied to the tag filters.
     * @param groupID The identifier of the group of filters.
     */
    public data class Numeric(
        override val filterState: FilterState,
        override val viewModel: FilterListViewModel.Numeric = FilterListViewModel.Numeric(),
        override val groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
    ) : FilterListConnector<Filter.Numeric>() {

        override val connectionFilterState: Connection = viewModel.connectFilterState(filterState, groupID)

        /**
         * @param filters The logic applied to the tag filters.
         * @param filterState The FilterState that will hold your filters.
         * @param selectionMode Whether the list can have Single or Multiple selections.
         * @param groupID The identifier of the group of filters.
         */
        public constructor(
            filters: List<Filter.Numeric>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Single,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
        ) : this(filterState, FilterListViewModel.Numeric(filters, selectionMode), groupID)

        init {
            traceNumericFilterListConnector()
        }
    }

    /**
     * [Documentation](https://www.algolia.com/doc/api-reference/widgets/filter-list-tag/android/)
     *
     * @param filterState The FilterState that will hold your filters.
     * @param viewModel The logic applied to the tag filters.
     * @param groupID The identifier of the group of filters.
     */
    public data class Tag(
        override val filterState: FilterState,
        override val viewModel: FilterListViewModel.Tag = FilterListViewModel.Tag(),
        override val groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
    ) : FilterListConnector<Filter.Tag>() {

        override val connectionFilterState: Connection = viewModel.connectFilterState(filterState, groupID)

        /**
         * @param filters The logic applied to the tag filters.
         * @param filterState The FilterState that will hold your filters.
         * @param selectionMode Whether the list can have Single or Multiple selections.
         * @param groupID The identifier of the group of filters.
         */
        public constructor(
            filters: List<Filter.Tag>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Multiple,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
        ) : this(filterState, FilterListViewModel.Tag(filters, selectionMode), groupID)

        init {
            traceTagFilterListConnector()
        }
    }

    /**
     * [Documentation](https://www.algolia.com/doc/api-reference/widgets/filter-list/android/)
     *
     * @param filterState The FilterState that will hold your filters.
     * @param viewModel The logic applied to the tag filters.
     * @param groupID The identifier of the group of filters.
     */
    public data class All(
        override val filterState: FilterState,
        override val viewModel: FilterListViewModel.All = FilterListViewModel.All(),
        override val groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
    ) : FilterListConnector<Filter>() {

        override val connectionFilterState: Connection = viewModel.connectFilterState(filterState, groupID)

        /**
         * @param filters The logic applied to the tag filters.
         * @param filterState The FilterState that will hold your filters.
         * @param selectionMode Whether the list can have Single or Multiple selections.
         * @param groupID The identifier of the group of filters.
         */
        public constructor(
            filters: List<Filter>,
            filterState: FilterState,
            selectionMode: SelectionMode = SelectionMode.Multiple,
            groupID: FilterGroupID = FilterGroupID(FilterOperator.And),
        ) : this(filterState, FilterListViewModel.All(filters, selectionMode), groupID)

        init {
            traceFilterListConnector()
        }
    }
}
