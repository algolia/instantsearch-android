package com.algolia.instantsearch.helper.filter.current

import com.algolia.search.model.filter.Filter


public fun CurrentFiltersViewModel.connectView(view: CurrentFiltersView) {
    val onNewMap: (Map<String, Filter>) -> Unit = { view.setItems(it) }

    onNewMap(item)
    this.onMapComputed += onNewMap
    this.onItemChanged += onNewMap
    view.onClick = { remove(it) }
}