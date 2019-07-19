package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun SearchBoxViewModel.connectView(
    view: SearchBoxView,
    connect: Boolean = true
): Connection {
    return SearchBoxConnectionView(this, view).autoConnect(connect)
}