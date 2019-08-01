package com.algolia.instantsearch.core.tree

import com.algolia.instantsearch.core.subscription.SubscriptionValue


public abstract class TreeViewModel<K, V>(
    tree: Tree<V> = Tree()
) {

    public val tree = SubscriptionValue(tree)

    /**
     * Computes selected levels given a hierarchical key.
     *
     * @param key a hierarchy level
     */
    public abstract fun computeSelections(key: K)
}