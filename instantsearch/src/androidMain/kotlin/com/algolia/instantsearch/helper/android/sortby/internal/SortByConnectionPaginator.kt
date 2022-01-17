package com.algolia.instantsearch.helper.android.sortby.internal

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.helper.android.list.Paginator
import com.algolia.instantsearch.helper.sortby.SortByViewModel

/**
 * Connection between [Paginator] and [SortByViewModel].
 *
 * @param paginator paginator handler to connect
 * @param viewModel view model to connect
 */
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
