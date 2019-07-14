package com.algolia.instantsearch.helper.filter.current

import com.algolia.instantsearch.core.observable.ObservableKey
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl


public fun CurrentFiltersViewModel.connectView(
    view: CurrentFiltersView,
    presenter: FilterPresenter = FilterPresenterImpl(),
    key: ObservableKey? = null
) {
    map.subscribePast(key) {
        view.setItem(it.mapValues { (_, value) -> presenter(value) })
    }
    view.onClick = { remove(it) }
}