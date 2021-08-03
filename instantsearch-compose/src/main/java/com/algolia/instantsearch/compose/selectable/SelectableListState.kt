@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.selectable

import com.algolia.instantsearch.compose.selectable.internal.SelectableListStateImpl
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.core.selectable.list.SelectableListView

/**
 * [SelectableListView] for compose.
 */
public interface SelectableListState<T> : SelectableListView<T> {

    /**
     * List of selectable items.
     */
    public val items: List<SelectableItem<T>>
}

/**
 * Creates an instance of [SelectableListState].
 *
 * @param items initial selectable list items
 */
public fun <T> SelectableListState(items: List<SelectableItem<T>> = emptyList()): SelectableListState<T> {
    return SelectableListStateImpl(items)
}
