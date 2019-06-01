package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.clickable.ClickableView


public interface SelectableListView<T>: ClickableView<T> {

    public fun setSelectableItems(selectableItems: List<SelectableItem<T>>)
}