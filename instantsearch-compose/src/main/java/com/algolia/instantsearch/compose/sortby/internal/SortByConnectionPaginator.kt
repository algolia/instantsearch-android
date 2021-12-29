package com.algolia.instantsearch.compose.sortby.internal

import com.algolia.instantsearch.ExperimentalInstantSearch
import com.algolia.instantsearch.compose.list.Paginator
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.sortby.SortByViewModel

/**
 * Connection between [Paginator] and [SortByViewModel].
 *
 * @param viewModel SortBy ViewModel to connect
 * @param paginator PagingData handler to connect
 */
@ExperimentalInstantSearch
internal class SortByConnectionPaginator<T : Any>(
    private val viewModel: SortByViewModel,
    private val paginator: Paginator<T>,
) : ConnectionImpl() {

    private val onSelection: Callback<Int?> = {
        paginator.invalidate()
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
