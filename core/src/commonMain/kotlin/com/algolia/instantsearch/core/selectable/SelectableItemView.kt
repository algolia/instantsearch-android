package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.clickable.ClickableView


public interface SelectableItemView: ClickableView<Boolean> {

    public fun setIsSelected(isSelected: Boolean)

    public fun setText(text: String)
}