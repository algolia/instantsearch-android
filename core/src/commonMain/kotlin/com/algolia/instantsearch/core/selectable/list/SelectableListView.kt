package com.algolia.instantsearch.core.selectable.list

import com.algolia.instantsearch.core.Callback

/**
 * A View that can display a list of selectable items, and might allow the user to select them.
 */
public interface SelectableListView<T> {

    /**
     * A callback that you must call when the selections changes.
     */
    public var onSelection: Callback<T>?

    /**
     * Updates the selectable items.
     */
    public fun setItems(items: List<SelectableItem<T>>)
}