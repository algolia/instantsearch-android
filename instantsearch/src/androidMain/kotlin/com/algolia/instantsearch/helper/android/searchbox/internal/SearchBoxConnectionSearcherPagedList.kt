package com.algolia.instantsearch.helper.android.searchbox.internal

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searchbox.SearchMode

internal data class SearchBoxConnectionSearcherPagedList<R>(
    private val viewModel: SearchBoxViewModel,
    private val searcher: Searcher<R>,
    private val pagedList: List<LiveData<out PagedList<out Any>>>,
    private val searchMode: SearchMode,
    private val debouncer: Debouncer,
) : ConnectionImpl() {

    private val searchAsYouType: Callback<String?> = { query ->
        searcher.setQuery(query)
        debouncer.debounce(searcher) {
            pagedList.forEach {
                it.value?.dataSource?.invalidate()
            }
        }
    }
    private val searchOnSubmit: Callback<String?> = { query ->
        searcher.setQuery(query)
        pagedList.forEach {
            it.value?.dataSource?.invalidate()
        }
    }

    override fun connect() {
        super.connect()
        when (searchMode) {
            SearchMode.AsYouType -> viewModel.query.subscribe(searchAsYouType)
            SearchMode.OnSubmit -> viewModel.eventSubmit.subscribe(searchOnSubmit)
        }
    }

    override fun disconnect() {
        super.disconnect()
        when (searchMode) {
            SearchMode.AsYouType -> viewModel.query.unsubscribe(searchAsYouType)
            SearchMode.OnSubmit -> viewModel.eventSubmit.unsubscribe(searchOnSubmit)
        }
    }
}
