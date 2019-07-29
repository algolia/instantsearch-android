package com.algolia.instantsearch.helper.android.searchbox

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.connection.ConnectionImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher
import com.algolia.instantsearch.helper.searchbox.SearchMode


public class SearchBoxWidgetPagedList<R>(
    public val searcher: Searcher<R>,
    public val pagedList: List<LiveData<out PagedList<out Any>>>,
    public val viewModel: SearchBoxViewModel = SearchBoxViewModel(),
    public val searchMode: SearchMode = SearchMode.AsYouType,
    public val debouncer: Debouncer = Debouncer(100)
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