package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.item.connectView
import com.algolia.instantsearch.core.observable.ObservableKey


public fun <K, V> MapViewModel<K, V>.connectView(view: MapView<K, V>, key: ObservableKey? = null) {
    map.connectView(view, key) { it }
    view.onClick = (::remove)
}