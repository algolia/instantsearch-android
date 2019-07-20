package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.map.MapViewModel
import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableItem


public open class SelectableMapViewModel<K, V>(
    map: Map<K, V>
): MapViewModel<K, V>(map) {

    public val selected = ObservableItem<K?>(null)
    public val eventSelection = ObservableEvent<K?>()
}