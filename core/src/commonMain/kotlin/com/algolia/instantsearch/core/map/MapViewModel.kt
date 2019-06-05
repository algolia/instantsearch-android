package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.event.EventViewModel
import com.algolia.instantsearch.core.item.ItemViewModel


public open class MapViewModel<K, V>(
    items: Map<K, V>
) : ItemViewModel<Map<K, V>>(items), EventViewModel<K> {

//TODO: alias item for internal DX
//    var map
//        get() = item
//        set(value) {
//            item = value
//        }

    override val onTriggered: MutableList<(K) -> Unit> = mutableListOf()
}

