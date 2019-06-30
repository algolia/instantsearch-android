package com.algolia.instantsearch.helper.hierarchical


public fun <T> HierarchicalViewModel.connectView(view: HierarchicalView<T>, presenter: HierarchicalPresenter<T>) {
    onItemChanged += {
        view.setItem(presenter(it))
    }
    view.onClick = { computeSelections(it) }
}