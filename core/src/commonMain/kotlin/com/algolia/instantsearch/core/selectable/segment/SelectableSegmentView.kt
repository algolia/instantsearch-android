package com.algolia.instantsearch.core.selectable.segment

import com.algolia.instantsearch.core.clickable.ClickableView


public interface SelectableSegmentView<K, V>: ClickableView<K> {

    fun setSelected(selected: K?)

    fun setItems(items: Map<K, V>)
}