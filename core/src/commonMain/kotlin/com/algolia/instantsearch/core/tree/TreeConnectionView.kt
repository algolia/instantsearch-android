package com.algolia.instantsearch.core.tree

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl


internal data class TreeConnectionView<K, I, O>(
    private val viewModel: TreeViewModel<K, I>,
    private val view: TreeView<K, O>,
    private val presenter: TreePresenter<I, O>
): ConnectionImpl() {

    private val updateItem: Callback<Tree<I>> = { tree ->
        println("here")
        view.setTree(presenter(tree))
    }

    override fun connect() {
        super.connect()
        viewModel.tree.subscribePast(updateItem)
        view.onSelectionChanged = { selection -> viewModel.computeSelections(selection) }
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.tree.unsubscribe(updateItem)
        view.onSelectionChanged = null
    }
}