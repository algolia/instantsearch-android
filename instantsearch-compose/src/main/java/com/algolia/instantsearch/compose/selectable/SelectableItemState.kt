@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.selectable

import com.algolia.instantsearch.compose.selectable.internal.SelectableItemStateImpl
import com.algolia.instantsearch.compose.selectable.list.SelectableListState
import com.algolia.instantsearch.core.selectable.SelectableItemView
import com.algolia.instantsearch.core.selectable.list.SelectableItem

/**
 * [SelectableItemView] for compose.
 */
public interface SelectableItemState<T> : SelectableItemView<T> {

    /**
     * Value item state.
     */
    public val item: T

    /**
     * Selection state.
     */
    public val isSelected: Boolean
}

/**
 * Creates an instance of [SelectableListState].
 *
 * @param selectableItem initial selectable item value
 */
public fun <T> SelectableItemState(selectableItem: SelectableItem<T>): SelectableItemState<T> {
    return SelectableItemStateImpl(selectableItem.first, selectableItem.second)
}

/**
 * Creates an instance of [SelectableListState].
 *
 * @param item initial selectable item
 * @param isSelected initial selection state value
 */
public fun <T> SelectableItemState(item: T, isSelected: Boolean = false): SelectableItemState<T> {
    return SelectableItemStateImpl(item, isSelected)
}

/**
 * Selectable item value.
 */
public val <T>SelectableItemState<T>.selectableItem: SelectableItem<T>
    get() = item to isSelected
