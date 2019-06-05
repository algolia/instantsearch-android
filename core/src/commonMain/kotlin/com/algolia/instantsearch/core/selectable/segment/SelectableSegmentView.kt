package com.algolia.instantsearch.core.selectable.segment

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface SelectableSegmentView<K, V>:  ItemView<Map<K, V>>, EventView<K> {

    public fun setSelected(selected: K?)
}