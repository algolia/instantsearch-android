package com.algolia.instantsearch.helper.filter.clear


public fun ClearFiltersViewModel.connectView(view: ClearFiltersView) {
    view.onClick = {
        clearFilters()
    }
}