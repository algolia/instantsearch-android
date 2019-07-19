package com.algolia.instantsearch.helper.index

import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun IndexSegmentViewModel.connectSearcher(searcher: SearcherSingleIndex) {
    segment.value[selected.value]?.let { searcher.index = it }
    eventSelection.subscribe { computed ->
        segment.value[computed]?.let { searcher.index = it }
        searcher.searchAsync()
    }
}