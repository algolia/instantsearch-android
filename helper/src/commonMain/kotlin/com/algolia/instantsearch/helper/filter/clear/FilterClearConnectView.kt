package com.algolia.instantsearch.helper.filter.clear

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun FilterClearViewModel.connectView(
    view: FilterClearView,
    connect: Boolean = true
): Connection {
    return FilterClearConnectionView(this, view).autoConnect(connect)
}