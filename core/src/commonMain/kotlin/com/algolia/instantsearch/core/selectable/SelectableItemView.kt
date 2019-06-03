package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.clickable.ClickableView
import com.algolia.instantsearch.core.item.ItemView


public interface SelectableItemView: ClickableView<Boolean>, ItemView<String> {

    public fun setIsSelected(isSelected: Boolean)
}