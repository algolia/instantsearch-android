package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun <T> SelectableListViewModel<T, T>.connectView(
    view: SelectableListView<T>,
    connect: Boolean = true
): Connection {
    return SelectableListConnectionView(this, view).autoConnect(connect)
}