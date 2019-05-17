package com.algolia.instantsearch.core.selectable


public interface SelectableItemView {

    var onClick: ((Boolean) -> Unit)?

    fun setIsSelected(isSelected: Boolean)

    fun setText(text: String)
}