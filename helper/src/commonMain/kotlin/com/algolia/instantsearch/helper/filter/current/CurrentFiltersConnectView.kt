package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.item.connectView


public fun CurrentFiltersViewModel.connectView(view: CurrentFiltersView) {
    connectView(view) { it }
    view.onClick  = { remove(it) }
}