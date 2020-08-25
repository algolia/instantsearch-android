package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.Callback


public interface SelectableItemView<T> {

    public var onSelectionChanged: Callback<Boolean>?

    public fun setItem(item: T)
    public fun setIsSelected(isSelected: Boolean)
}