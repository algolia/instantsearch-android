package com.algolia.instantsearch.helper.android.searchbox

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.core.searcher.debounceSearchInMillis
import com.algolia.instantsearch.helper.searchbox.SearchMode


public data class SearchBoxConnectorPagedList<R>(
    @JvmField
    public val searcher: Searcher<R>,
    @JvmField
    public val pagedList: List<LiveData<out PagedList<out Any>>>,
    @JvmField
    public val viewModel: SearchBoxViewModel = SearchBoxViewModel(),
    @JvmField
    public val searchMode: SearchMode = SearchMode.AsYouType,
    @JvmField
    public val debouncer: Debouncer = Debouncer(debounceSearchInMillis)
) : ConnectionImpl() {

    private val connectionSearcher = viewModel.connectSearcher(searcher, pagedList, searchMode, debouncer)

    override fun connect() {
        super.connect()
        connectionSearcher.connect()
    }

    override fun disconnect() {
        super.disconnect()
        connectionSearcher.disconnect()
    }
}