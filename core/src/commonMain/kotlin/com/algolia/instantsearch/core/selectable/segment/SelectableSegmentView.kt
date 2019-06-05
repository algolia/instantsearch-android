package com.algolia.instantsearch.core.selectable.segment

import com.algolia.instantsearch.core.event.EventView


public interface SelectableSegmentView<K, V>: EventView<K> {

    public fun setSelected(selected: K?)

    public fun setItems(items: Map<K, V>)
}