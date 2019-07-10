package com.algolia.instantsearch.helper.hierarchical

import com.algolia.instantsearch.core.tree.TreeView


public fun <T> HierarchicalViewModel.connectView(view: TreeView<T>, presenter: HierarchicalPresenter<T>) {
    onItemChanged += {
        view.setItem(presenter(it))
    }
    view.onClick = { computeSelections(it) }
}