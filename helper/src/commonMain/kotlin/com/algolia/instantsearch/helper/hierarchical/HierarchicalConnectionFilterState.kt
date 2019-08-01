package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterOperator
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.filter.Filter


internal data class HierarchicalConnectionFilterState(
    private val viewModel: HierarchicalViewModel,
    private val filterState: FilterState
): ConnectionImpl() {

    private val filterGroupID = FilterGroupID(hierarchicalGroupName, FilterOperator.And)

    private val updateSelections: Callback<Filters> = { filters ->
        viewModel.selections.value = (filters.getFacetFilters(filterGroupID).first().value as Filter.Facet.Value.String).raw.split(viewModel.separator)
    }

    private val updateFilterState: Callback<HierarchicalPath> = { selections ->
        filterState.notify {
            val last = selections.last()
            val filter = Filter.Facet(last.first, last.second)
            val filters = selections.map { Filter.Facet(it.first, it.second) }

            filterState.clear(filterGroupID)
            filterState.add(filterGroupID, filter)
            filterState.hierarchicalFilters = filters
        }
    }

    init {
        filterState.hierarchicalAttributes = viewModel.hierarchicalAttributes
    }

    override fun connect() {
        super.connect()
        filterState.filters.subscribe(updateSelections)
        viewModel.eventHierarchicalPath.subscribe(updateFilterState)
    }

    override fun disconnect() {
        super.disconnect()
        filterState.filters.unsubscribe(updateSelections)
        viewModel.eventHierarchicalPath.unsubscribe(updateFilterState)
    }
}