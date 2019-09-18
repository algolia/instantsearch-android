@file:JvmName("SelectableMap")

package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import kotlin.jvm.JvmName


/**
 * Connects this SelectableMapViewModel to a SelectableMapView,
 * updating it when the items or the selections changes.
 */
public fun <K, I, O> SelectableMapViewModel<K, I>.connectView(
    view: SelectableMapView<K, O>,
    presenter: Presenter<I, O>
): Connection {
    return SelectableMapConnectionView(this, view, presenter)
}