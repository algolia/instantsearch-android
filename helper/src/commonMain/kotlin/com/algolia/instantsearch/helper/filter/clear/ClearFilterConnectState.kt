package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.Searcher

public fun ClearFilterViewModel.connectState(
    filterState: FilterState,
    queryToClearSearcher: Searcher? = null
) {
    onCleared += {
        queryToClearSearcher?.setQuery("")
        filterState.notify { clear() }
    }
}