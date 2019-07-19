package com.algolia.instantsearch.core.selectable.segment

import com.algolia.instantsearch.core.event.Event


public interface SelectableSegmentView<K, V> {

    public var onSelectionChange: Event<K>

    public fun setSegment(segment: Map<K, V>)

    public fun setSelected(selected: K?)
}