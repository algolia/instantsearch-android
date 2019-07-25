package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun SearchBoxViewModel.connectionView(
    view: SearchBoxView
): Connection {
    return SearchBoxConnectionView(this, view)
}