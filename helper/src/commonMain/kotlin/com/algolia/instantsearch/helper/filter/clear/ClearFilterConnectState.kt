package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.search.model.search.Query

public fun ClearFilterViewModel.connectState(filterState: FilterState, queryToClear: Query? = null) {
    onCleared += {
        queryToClear?.query = ""
        filterState.notify { clear() }
        println("Clear all notified!, query was ${queryToClear?.query}")
    }
}