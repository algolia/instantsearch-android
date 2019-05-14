package com.algolia.instantsearch.helper.widget

import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun HitsViewModel.connectSearcher(searcher: SearcherSingleIndex) {
    searcher.onResponseChanged += {
        items = it.hits
    }
}