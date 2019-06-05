package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.item.ItemViewModel


public open class MapViewModel<K, V>(
    items: Map<K, V>
) : ItemViewModel<Map<K, V>>(items) {

//TODO: alias item for internal DX
//    var map
//        get() = item
//        set(value) {
//            item = value
//        }
}

