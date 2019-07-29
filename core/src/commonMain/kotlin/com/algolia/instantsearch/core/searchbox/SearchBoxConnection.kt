package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.connection.Connection


public fun SearchBoxViewModel.connectionView(
    view: SearchBoxView
): Connection {
    return SearchBoxConnectionView(this, view)
}