package com.algolia.instantsearch.helper.android.filter.state

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.filter.state.Filters


internal data class FilterStateConnectionPagedList<T>(
    private val pagedList: LiveData<PagedList<T>>,
    private val filterState: FilterState
) : ConnectionImpl() {

    private val updateFilterState: Callback<Filters> = {
        pagedList.value?.dataSource?.invalidate()
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