@file:JvmName("SearchBox")
@file:JvmMultifileClass

package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.connection.Connection
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


/**
 * Connects this SearchBoxViewModel to a SearchBoxView, updating the displayed query
 * when the viewModel's data changes and updating this data when the user interacts with the view.
 */
public fun SearchBoxViewModel.connectView(
    view: SearchBoxView
): Connection {
    return SearchBoxConnectionView(this, view)
}