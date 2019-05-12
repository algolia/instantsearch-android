package com.algolia.instantsearch.core.selectable


public interface SelectableView {

    var onClick: ((Boolean) -> Unit)?

    fun setIsSelected(isSelected: Boolean)

    fun setText(text: String)
}