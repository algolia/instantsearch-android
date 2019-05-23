package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


fun IndexSegmentViewModel.connectSearcher(searcher: SearcherSingleIndex) {
    items[selected]?.let { searcher.index = it }
    onSelectedComputed += { computed ->
        items[computed]?.let { searcher.index = it}
        searcher.search()
    }
}