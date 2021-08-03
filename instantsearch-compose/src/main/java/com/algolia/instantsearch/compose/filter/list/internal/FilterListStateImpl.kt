package com.algolia.instantsearch.compose.filter.list.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.filter.list.FilterListState
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.search.model.filter.Filter

/**
 * [FilterListState] implementation.
 *
 * @param filters initial filters list value
 */
internal class FilterListStateImpl<T : Filter>(
    items: List<SelectableItem<T>>
) : FilterListState<T> {

    @set:JvmName("_items")
    override var items: List<SelectableItem<T>> by mutableStateOf(items)
    override var onSelection: Callback<T>? = null

    override fun setItems(items: List<SelectableItem<T>>) {
        this.items = items
    }
}
