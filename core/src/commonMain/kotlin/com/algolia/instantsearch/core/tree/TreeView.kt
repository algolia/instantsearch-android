package com.algolia.instantsearch.core.tree

import com.algolia.instantsearch.core.Callback

/**
 * A View that can display a tree of values, and might allow the user to select/deselect one of them.
 */
public interface TreeView<K, V> {

    /**
     * Updates the tree to display.
     */
    fun setTree(tree: V)

    /**
     * A callback that you must call when the selection changes.
     */
    var onSelectionChanged: Callback<K>?
}