package com.algolia.instantsearch.core.selectable.segment


public interface SelectableSegmentView<K, V> {

    var onClick: ((K) -> Unit)?

    fun setSelected(selected: K?)

    fun setItems(items: Map<K, V>)
}