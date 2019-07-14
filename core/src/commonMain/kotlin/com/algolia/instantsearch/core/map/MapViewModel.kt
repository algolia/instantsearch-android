package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.observable.ObservableItem


public open class MapViewModel<K, V>(
    items: Map<K, V> = mapOf()
) {

    public val map = ObservableItem(items)

    public val onMapComputed: MutableList<(Map<K, V>) -> Unit> = mutableListOf()

    public fun remove(key: K) {
        val map = map.get().toMutableMap().apply { remove(key) }

        onMapComputed.forEach { it(map) }
    }

    public fun clear() {
        val map = map.get().toMutableMap().apply { clear() }

        onMapComputed.forEach { it(map) }
    }
}