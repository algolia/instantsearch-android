package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.Callback

/**
 * A View that can display a selectable item,
 * and might allow the user to select/deselect it.
 *
 */
public interface SelectableItemView<T> {

    /**
     * A callback that you must call when the selection changes.
     */
    public var onSelectionChanged: Callback<Boolean>?

    /**
     * Updates the item to display.
     */
    public fun setItem(item: T)

    /**
     * Updates the selection state.
     */
    public fun setIsSelected(isSelected: Boolean)
}