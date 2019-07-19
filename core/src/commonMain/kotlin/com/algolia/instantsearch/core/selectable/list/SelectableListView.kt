package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.event.Event


public interface SelectableListView<T> {

    public var onSelection: Event<T>

    public fun setItems(items: List<SelectableItem<T>>)
}