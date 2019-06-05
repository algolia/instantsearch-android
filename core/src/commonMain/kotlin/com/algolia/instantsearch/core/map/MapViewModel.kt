package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.item.ItemViewModel


public open class MapViewModel<K, V>(
    items: Map<K, V>
) : ItemViewModel<Map<K, V>>(items) {

    //TODO: alias item=map for internal DX

    val onMapComputed: MutableList<(Map<K, V>) -> Unit> = mutableListOf()

    fun remove(key: K) {
        val map = item.toMutableMap().apply { remove(key) }

        onMapComputed.forEach { it(map) }
    }

    fun getValues() = item.values.toSet()
}

