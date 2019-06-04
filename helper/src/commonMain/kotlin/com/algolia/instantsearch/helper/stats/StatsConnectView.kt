package com.algolia.instantsearch.helper.stats

import com.algolia.instantsearch.core.item.ItemView
import com.algolia.instantsearch.core.item.connectView


public fun StatsViewModel.connectView(view: ItemView<String>, presenter: StatsPresenter<String>) {
    connectView(view, presenter)
}