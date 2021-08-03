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
    filters: List<SelectableItem<T>>
) : FilterListState<T> {

    override var filters: List<SelectableItem<T>> by mutableStateOf(filters)
    override var onSelection: Callback<T>? = null

    override fun setItems(items: List<SelectableItem<T>>) {
        this.filters = items
    }
}
