package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.Presenter
import com.algolia.instantsearch.core.connection.Connection


public fun <K, I, O> SelectableMapViewModel<K, I>.connectView(
    view: SelectableMapView<K, O>,
    presenter: Presenter<I, O>
): Connection {
    return SelectableMapConnectionView(this, view, presenter)
}