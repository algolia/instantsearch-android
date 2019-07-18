package com.algolia.instantsearch.helper.filter.current


public fun FilterCurrentViewModel.connectView(
    view: FilterCurrentView,
    presenter: FilterCurrentPresenter = FilterCurrentPresenterImpl()
) {
    map.subscribePast { view.setItem(presenter(it )) }
    view.onClick = (::remove)
}