package com.algolia.instantsearch.hierarchical.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.Filters
import com.algolia.instantsearch.hierarchical.HierarchicalFilter
import com.algolia.instantsearch.hierarchical.HierarchicalPath
import com.algolia.instantsearch.hierarchical.HierarchicalViewModel
import com.algolia.search.model.filter.Filter

internal data class HierarchicalConnectionFilterState(
    private val viewModel: HierarchicalViewModel,
    private val filterState: FilterState,
) : ConnectionImpl() {

    private val updateSelections: Callback<Filters> = { filters ->
        val hierarchicalFilter = filters.getHierarchicalFilters(viewModel.attribute)
        val hierarchicalValue = hierarchicalFilter?.filter?.value as? Filter.Facet.Value.String

        viewModel.selections.value = hierarchicalValue?.raw?.split(viewModel.separator) ?: emptyList()
    }

    private val updateFilterState: Callback<HierarchicalPath> = { selections ->
        filterState.notify {
            val last = selections.last()
            val filter = Filter.Facet(last.first, last.second)
            val path = selections.map { Filter.Facet(it.first, it.second) }
            val hierarchicalFilter = HierarchicalFilter(
                attributes = viewModel.hierarchicalAttributes,
                filter = filter,
                path = path
            )

            add(viewModel.attribute, hierarchicalFilter)
        }
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
