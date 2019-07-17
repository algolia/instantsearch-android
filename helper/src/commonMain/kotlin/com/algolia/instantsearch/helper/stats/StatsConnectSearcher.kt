package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex


public fun StatsViewModel.connectSearcher(searcher: SearcherSingleIndex) {
    searcher.response.subscribePast { item = it }
}