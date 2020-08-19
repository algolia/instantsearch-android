package com.algolia.instantsearch.core.tree

import com.algolia.instantsearch.core.Callback


public interface TreeView<K, V> {

    public fun setTree(tree: V)

    public var onSelectionChanged: Callback<K>?
}
