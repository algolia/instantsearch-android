package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.helper.filter.state.FilterGroupID
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters
import com.algolia.search.model.filter.Filter


internal class FilterCurrentConnectionFilterState(
    private val viewModel: FilterCurrentViewModel,
    private val filterState: FilterState,
    private val groupID: FilterGroupID?
) : ConnectionImpl() {

    private val updateMap: Callback<Filters> = { filters ->
        val groups = filters.getGroups().filter { groupID == null || it.key == groupID }
        val filterAndIDs = groups.toFilterAndIds()

        viewModel.map.value = filterAndIDs
    }
    private val updateFilters: Callback<Map<FilterAndID, Filter>> = { map ->
        filterState.notify {
            if (groupID != null) {
                clear(groupID)
            } else {
                clear()
            }
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