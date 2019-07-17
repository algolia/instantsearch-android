package com.algolia.instantsearch.helper.android.list

import com.algolia.instantsearch.helper.filter.state.FilterState

public fun <T> SearcherSingleIndexDataSource.Factory<T>.connectFilterState(filterState: FilterState) {
    println("Connecting")
    filterState.onChanged += {
        println("Invalidating data!")
        lastDataSource.invalidate()
    }
}