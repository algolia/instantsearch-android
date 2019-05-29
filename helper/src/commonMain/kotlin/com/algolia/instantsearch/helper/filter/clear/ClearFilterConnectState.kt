package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.helper.filter.state.FilterState


public fun ClearFilterViewModel.connectFilterState(filterState: FilterState) {
    onCleared += {
        filterState.notify { clear() }
    }
}