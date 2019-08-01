package com.algolia.instantsearch.core.tree

import com.algolia.instantsearch.core.Callback


public interface TreeView<K, V> {

    fun setTree(tree: V)

    var onSelectionChanged: Callback<K>?
}