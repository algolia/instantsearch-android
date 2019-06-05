package com.algolia.instantsearch.helper.filter.current

import com.algolia.search.model.filter.Filter


public fun CurrentFiltersViewModel.connectView(view: CurrentFiltersView) {
    val onItemChange: (Map<String, Filter>) -> Unit = {
        view.setItems(item)
    }

    onItemChange(item)
    this.onItemChanged += onItemChange
    view.onClick = { remove(it) }
}