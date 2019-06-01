package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.clickable.ClickableView


public interface SelectableItemView: ClickableView<Boolean> {

    fun setIsSelected(isSelected: Boolean)

    fun setText(text: String)
}