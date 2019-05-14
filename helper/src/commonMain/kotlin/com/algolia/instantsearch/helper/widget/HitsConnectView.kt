package com.algolia.instantsearch.helper.widget

import com.algolia.instantsearch.core.HitsView
import com.algolia.search.model.response.ResponseSearch


public fun HitsViewModel.connectView(view: HitsView<ResponseSearch.Hit>) {
    fun setItems(hits: List<ResponseSearch.Hit>) {
        view.setHits(hits)
    }

    setItems(items)
    onItemsChanged += { items -> setItems(items) }
}