package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.item.ItemView
import com.algolia.search.model.response.ResponseSearch


public fun <T> StatsViewModel.connectView(view: ItemView<T>, presenter: StatsPresenter<T>) {
    val onItemChanged : (ResponseSearch?) -> Unit = { item ->
        view.setItem(presenter(item))
    }

    onItemChanged(item)
    this.onItemChanged += onItemChanged
}