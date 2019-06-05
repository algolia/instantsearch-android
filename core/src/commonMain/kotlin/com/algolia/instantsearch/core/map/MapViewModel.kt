package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.item.ItemViewModel


public open class MapViewModel<K, V>(
    items: Map<K, V> = mapOf()
) : ItemViewModel<Map<K, V>>(items) {

    var map: Map<K, V>
        get() = item
        set(value) {
            item = value
        }
    val onMapComputed: MutableList<(Map<K, V>) -> Unit> = mutableListOf()

    fun remove(key: K) {
        val map = item.toMutableMap().apply { remove(key) }

        onMapComputed.forEach { it(map) }
    }
}

