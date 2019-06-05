package com.algolia.instantsearch.core.map

import com.algolia.instantsearch.core.event.EventView


public interface MapView<K, V>: EventView<K> {

    public fun setItems(items: Map<K, V>)
}