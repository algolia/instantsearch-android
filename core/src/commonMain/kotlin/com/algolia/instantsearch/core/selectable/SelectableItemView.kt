package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface SelectableItemView<T> : EventView<Boolean>, ItemView<T> {

    public fun setIsSelected(isSelected: Boolean)
}