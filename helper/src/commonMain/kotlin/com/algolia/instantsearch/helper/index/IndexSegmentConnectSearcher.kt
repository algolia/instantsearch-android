package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun IndexSegmentViewModel.connectSearcher(searcher: SearcherSingleIndex) {
    item[selected]?.let { searcher.index = it }
    onSelectedComputed += { computed ->
        item[computed]?.let { searcher.index = it}
        searcher.search()
    }
}