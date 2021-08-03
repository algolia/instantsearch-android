package com.algolia.instantsearch.compose.selectable.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.selectable.SelectableListState
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.list.SelectableItem

internal class SelectableListStateImpl<T>(
    items: List<SelectableItem<T>>
) : SelectableListState<T> {

    @set:JvmName("_items")
    override var items by mutableStateOf(items)
    override var onSelection: Callback<T>? = null

    override fun setItems(items: List<SelectableItem<T>>) {
        this.items = items
    }
}
