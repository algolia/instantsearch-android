package com.algolia.instantsearch.core.tree


public fun <K, I, O> TreeViewModel<K, I>.connectView(
    view: TreeView<K, O>,
    presenter: TreePresenter<I, O>
) {
    val setItem: (Tree<I>) -> Unit = {
        view.setItem(presenter(it))
    }

    setItem(item)
    onItemChanged += setItem
    view.onClick = { computeSelections(it) }
}