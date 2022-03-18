package com.algolia.instantsearch.android.paging3.filterstate.internal

import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters

/**
 * Connection between [Paginator] and [FilterState].
 *
 * @param paginator PagingData handler to connect
 * @param filterState FilterState to connect
 */
internal data class FilterStateConnectionPaginator<T : Any>(
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
