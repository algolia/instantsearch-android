package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl
import com.algolia.search.model.filter.Filter


public fun CurrentFiltersViewModel.connectView(
    view: CurrentFiltersView,
    presenter: FilterPresenter = FilterPresenterImpl()
) {
    val onNewMap: (Map<String, Filter>) -> Unit =
        { view.setItem(it.mapValues { (_, value) -> presenter.invoke(value) }) }

    onNewMap(item)
    this.onItemChanged += onNewMap
    view.onClick = { remove(it) }
}