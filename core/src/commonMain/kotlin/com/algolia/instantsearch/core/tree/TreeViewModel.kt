package com.algolia.instantsearch.core.tree

import com.algolia.instantsearch.core.item.ItemViewModel


abstract class TreeViewModel<K, V>(tree: Tree<V>) : ItemViewModel<Tree<V>>(tree) {

    /**
     * Computes selected levels given a hierarchical key.
     *
     * @param key a hierarchy level
     */
    public abstract fun computeSelections(key: K)
}