package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.observable.ObservableKey


public fun FilterCurrentViewModel.connectView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter = FilterCurrentPresenterImpl(),
    key: ObservableKey? = null
) {
    filters.subscribePast(key) { view.setItem(presenter(it)) }
    view.onClick = (::remove)
}