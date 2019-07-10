package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.tree.TreeView


public fun <T> HierarchicalViewModel.connectView(view: TreeView<T>, presenter: HierarchicalPresenter<T>) {
    val setItem: (HierarchicalTree) -> Unit = {
        view.setItem(presenter(it))
    }

    setItem(item)
    onItemChanged += setItem
    view.onClick = { computeSelections(it) }
}