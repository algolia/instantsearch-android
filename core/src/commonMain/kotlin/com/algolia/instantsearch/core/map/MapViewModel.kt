package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableItem


public open class MapViewModel<K, V>(
    items: Map<K, V> = mapOf()
) {

    public val map = ObservableItem(items)
    public val event = ObservableEvent<MapEvent<K, V>>()

    public fun add(entry: Pair<K, V>) {
        event.send(MapEvent.Add(map.value, entry))
    }

    public fun remove(key: K) {
        event.send(MapEvent.Remove(map.value, key))
    }

    public fun clear() {
        event.send(MapEvent.Clear(map.value))
    }
}