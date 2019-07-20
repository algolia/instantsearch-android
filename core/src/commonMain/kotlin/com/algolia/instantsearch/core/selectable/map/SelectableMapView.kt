package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.event.Event


public interface SelectableMapView<K, V> {

    public var onSelectionChange: Event<K>

    public fun setMap(map: Map<K, V>)

    public fun setSelected(selected: K?)
}