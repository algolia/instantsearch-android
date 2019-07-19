package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.event.Event


public interface SelectableItemView<T> {

    public var onSelectionChanged: Event<Boolean>

    public fun setItem(item: T)
    public fun setIsSelected(isSelected: Boolean)
}