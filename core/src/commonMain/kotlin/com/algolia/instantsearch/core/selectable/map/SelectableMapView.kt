package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.event.Callback


public interface SelectableMapView<K, V> {

    public var onSelectionChange: Callback<K>?

    public fun setMap(map: Map<K, V>)

    public fun setSelected(selected: K?)
}