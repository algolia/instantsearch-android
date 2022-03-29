package com.algolia.instantsearch.filter.current.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.filter.current.FilterAndID
import com.algolia.instantsearch.filter.current.FilterCurrentViewModel
import com.algolia.instantsearch.filter.state.FilterGroupID
import com.algolia.instantsearch.filter.state.FilterState
import com.algolia.instantsearch.filter.state.Filters
import com.algolia.search.model.filter.Filter

internal data class FilterCurrentConnectionFilterState(
    private val viewModel: FilterCurrentViewModel,
    private val filterState: FilterState,
    private val groupIDs: List<FilterGroupID>,
) : ConnectionImpl() {

    private val updateMap: Callback<Filters> = { filters ->
        val groups = filters.getGroups().filter { if (groupIDs.isNotEmpty()) groupIDs.contains(it.key) else true }
        val filterAndIDs = groups.toFilterAndIds()

        viewModel.map.value = filterAndIDs
    }
    private val updateFilters: Callback<Map<FilterAndID, Filter>> = { map ->
        filterState.notify {
            clear(*groupIDs.toTypedArray())
            map.forEach { add(it.key.first, it.value) }
        }
    }

    override fun connect() {
        super.connect()
        filterState.filters.subscribePast(updateMap)
        viewModel.event.subscribe(updateFilters)
    }

    override fun disconnect() {
        super.disconnect()
        filterState.filters.unsubscribe(updateMap)
        viewModel.event.unsubscribe(updateFilters)
    }
}
