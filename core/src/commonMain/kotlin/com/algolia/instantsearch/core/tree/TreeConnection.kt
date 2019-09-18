@file:JvmName("TreeUtils")

package com.algolia.instantsearch.core.tree

import com.algolia.instantsearch.core.connection.Connection
import kotlin.jvm.JvmName

/**
 * Connects this TreeViewModel to a TreeView, updating it when the tree changes.
 */
public fun <K, I, O> TreeViewModel<K, I>.connectView(
    view: TreeView<K, O>,
    presenter: TreePresenter<I, O>
): Connection {
    return TreeConnectionView(this, view, presenter)
}