package com.algolia.instantsearch.android.paging3.searchbox.internal

import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.helper.searchbox.SearchMode

/**
 * Connection implementation between [SearchBoxViewModel] and [Paginator].
 *
 * @param viewModel search box view model
 * @param paginator component handling Paged data
 * @param searchMode selected search mode
 */
internal class SearchBoxConnectionPaginator<T : Any>(
    private val viewModel: SearchBoxViewModel,
    private val paginator: Paginator<T>,
    private val searchMode: SearchMode,
) : ConnectionImpl() {

    private val querySubscription: (String?) -> Unit = { paginator.invalidate() }

    override fun connect() {
        super.connect()
        when (searchMode) {
            SearchMode.AsYouType -> viewModel.query.subscribe(querySubscription)
            SearchMode.OnSubmit -> viewModel.eventSubmit.subscribe(querySubscription)
        }
    }

    override fun disconnect() {
        super.disconnect()
        when (searchMode) {
            SearchMode.AsYouType -> viewModel.query.unsubscribe(querySubscription)
            SearchMode.OnSubmit -> viewModel.eventSubmit.unsubscribe(querySubscription)
        }
    }
}
