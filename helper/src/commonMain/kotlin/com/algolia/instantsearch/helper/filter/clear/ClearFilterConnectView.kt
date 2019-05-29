package com.algolia.instantsearch.helper.filter.clear


fun ClearFiltersViewModel.connectView(view: ClearFiltersView) {

    view.onClick = {
        this.clearFilters()
    }
}