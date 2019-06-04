package com.algolia.instantsearch.helper.filter.list

import com.algolia.instantsearch.core.selectable.list.connectView
import com.algolia.search.model.filter.Filter


public fun <T : Filter> FilterListViewModel<T>.connectView(
    view: FilterListView<T>
) {
    connectView(view)
}