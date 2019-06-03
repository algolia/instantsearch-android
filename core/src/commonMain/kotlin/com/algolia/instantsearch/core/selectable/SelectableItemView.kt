package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface SelectableItemView : EventView<Boolean>, ItemView<String> {

    public fun setIsSelected(isSelected: Boolean)
}