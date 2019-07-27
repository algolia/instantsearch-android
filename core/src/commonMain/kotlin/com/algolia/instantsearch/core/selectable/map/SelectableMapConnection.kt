package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun <K, I, O> SelectableMapViewModel<K, I>.connectionView(
    view: SelectableMapView<K, O>,
    presenter: Presenter<I, O>
): Connection {
    return SelectableMapConnectionView(this, view, presenter)
}