package com.algolia.instantsearch.helper.android.searchbox

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.algolia.instantsearch.core.searchbox.SearchBoxViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


fun <T> SearchBoxViewModel.connectSearcher(
    searcher: Searcher,
    pagedList: LiveData<PagedList<T>>,
    searchAsYouType: Boolean = true,
    debouncer: Debouncer = Debouncer(100)
) {
    if (searchAsYouType) {
        onItemChanged += {
            searcher.setQuery(it)
            debouncer.debounce(searcher) {
                pagedList.value?.dataSource?.invalidate()
            }
        }
    } else {
        onQuerySubmitted += {
            searcher.setQuery(it)
            pagedList.value?.dataSource?.invalidate()
        }
    }
}