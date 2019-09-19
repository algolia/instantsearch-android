package com.algolia.instantsearch.core.selectable.map

import com.algolia.instantsearch.core.Callback

/**
 * A View that can display a map of selectable items, and might allow the user to select one.
 */
public interface SelectableMapView<K, V> {

    /**
     * A callback that you must call when the selection changes.
     */
    public var onSelectionChange: Callback<K>?

    /**
     * Updates the map to display.
     */
    public fun setMap(map: Map<K, V>)

    /**
     * Updates the selected item.
     */
    public fun setSelected(selected: K?)
}