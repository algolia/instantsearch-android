package com.algolia.instantsearch.helper.android.searchbox

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.observable.ObservableKey
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


fun <T, R> SearchBoxViewModel.connectSearcher(
    searcher: Searcher<R>,
    pagedList: LiveData<PagedList<T>>,
    searchAsYouType: Boolean = true,
    debouncer: Debouncer = Debouncer(100),
    key: ObservableKey? = null
) {
    if (searchAsYouType) {
        query.subscribe(key) {
            searcher.setQuery(it)
            debouncer.debounce(searcher) {
                pagedList.value?.dataSource?.invalidate()
            }
        }
    } else {
        event.subscribe(key) {
            searcher.setQuery(it)
            pagedList.value?.dataSource?.invalidate()
        }
    }
}