package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.item.ItemView


public fun <T> StatsViewModel.connectView(view: ItemView<T>, presenter: StatsPresenter<T>) {
    view.setItem(presenter(item))
    onItemChanged += { item ->
        view.setItem(presenter(item))
    }
}