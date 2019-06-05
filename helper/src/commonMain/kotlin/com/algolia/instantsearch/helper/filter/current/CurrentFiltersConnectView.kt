package com.algolia.instantsearch.helper.filter.current

import com.algolia.search.model.filter.Filter


public fun CurrentFiltersViewModel.connectView(view: CurrentFiltersView) {
    val onNewMap: (Map<String, Filter>) -> Unit = { view.setItem(it) }

    onNewMap(item)
    this.onItemChanged += onNewMap
    view.onClick = { remove(it) }
}