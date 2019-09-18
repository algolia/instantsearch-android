@file:JvmName("SelectableList")

package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.connection.Connection
import kotlin.jvm.JvmName


/**
 * Connects this SelectableListViewModel to a SelectableListView,
 * updating it when the items or the selections changes.
 */
public fun <T> SelectableListViewModel<T, T>.connectView(
    view: SelectableListView<T>
): Connection {
    return SelectableListConnectionView(this, view)
}