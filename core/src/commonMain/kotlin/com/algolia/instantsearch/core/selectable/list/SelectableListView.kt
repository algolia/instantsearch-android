package com.algolia.instantsearch.core.selectable.list


public interface SelectableListView<T> {

    var onClick: ((T) -> Unit)?

    fun setSelectableItems(selectableItems: List<SelectableItem<T>>)
}