@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.filter.list

import com.algolia.instantsearch.compose.filter.list.internal.FilterListStateImpl
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.helper.filter.list.FilterListView
import com.algolia.search.model.filter.Filter

/**
 * [FilterListView] for compose.
 */
public interface FilterListState<T : Filter> : FilterListView<T> {

    /**
     * State holding selectable filters list value.
     */
    public val filters: List<SelectableItem<T>>

}

/**
 * Creates an instance of [FilterListState].
 *
 * @param filters initial selectable filters list
 */
public fun <T : Filter> FilterListState(filters: List<SelectableItem<T>> = emptyList()): FilterListState<T> {
    return FilterListStateImpl(filters)
}
