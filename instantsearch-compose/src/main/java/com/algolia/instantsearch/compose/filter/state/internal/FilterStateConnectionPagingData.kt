package com.algolia.instantsearch.compose.filter.state.internal

import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters

/**
 * Connection between [Paginator] and [FilterState].
 */
internal data class FilterStateConnectionPagingData<T : Any>(
    private val paginator: Paginator<T>,
    private val filterState: FilterState,
) : ConnectionImpl() {

    private val updateFilterState: Callback<Filters> = {
        paginator.invalidate()
    }

    override fun connect() {
        super.connect()
        filterState.filters.subscribe(updateFilterState)
    }

    override fun disconnect() {
        super.disconnect()
        filterState.filters.unsubscribe(updateFilterState)
    }
}
