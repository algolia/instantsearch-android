package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.searcher.Debouncer
import com.algolia.instantsearch.core.searcher.Searcher


public fun <R> LoadingViewModel.connectSearcher(
    searcher: Searcher<R>,
    debouncer: Debouncer = Debouncer(200)
) {
    isLoading.set(searcher.loading)
    event.subscribe { searcher.searchAsync() }
    searcher.onLoadingChanged += { loading ->
        debouncer.debounce(searcher) {
            isLoading.set(loading)
        }
    }
}