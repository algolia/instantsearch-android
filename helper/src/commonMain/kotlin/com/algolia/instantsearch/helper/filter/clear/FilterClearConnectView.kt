package com.algolia.instantsearch.helper.filter.clear


public fun FilterClearViewModel.connectView(view: FilterClearView) {
    view.onClick = { click() }
}
