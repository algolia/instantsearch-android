package com.algolia.instantsearch.helper.android.sortby.internal

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.sortby.searcher.SortByViewModel

internal class SortByConnectionPagedList<T>(
    private val viewModel: SortByViewModel,
    private val pagedList: LiveData<PagedList<T>>,
) : ConnectionImpl() {

    private val onSelection: Callback<Int?> = {
        pagedList.value?.dataSource?.invalidate()
    }

    override fun connect() {
        super.connect()
        viewModel.eventSelection.subscribe(onSelection)
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.eventSelection.unsubscribe(onSelection)
    }
}
