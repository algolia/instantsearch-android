package com.algolia.instantsearch.helper.loading

import com.algolia.instantsearch.core.loading.LoadingViewModel
import com.algolia.instantsearch.core.searcher.Searcher


public fun <R> LoadingViewModel.connectSearcher(searcher: Searcher<R>) {
    item = searcher.loading
    onTriggered += { searcher.searchAsync() }
    searcher.onLoadingChanged += { loading -> item = loading }
}