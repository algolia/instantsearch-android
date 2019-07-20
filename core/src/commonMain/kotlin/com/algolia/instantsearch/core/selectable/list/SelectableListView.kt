package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.event.Callback


public interface SelectableListView<T> {

    public var onSelection: Callback<T>?

    public fun setItems(items: List<SelectableItem<T>>)
}