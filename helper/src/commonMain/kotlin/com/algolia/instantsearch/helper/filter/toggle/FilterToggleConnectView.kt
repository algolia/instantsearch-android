package com.algolia.instantsearch.helper.filter.toggle

import com.algolia.instantsearch.core.selectable.connectView
import com.algolia.instantsearch.helper.filter.FilterPresenter
import com.algolia.instantsearch.helper.filter.FilterPresenterImpl


public fun FilterToggleViewModel.connectView(
    view: FilterToggleView,
    presenter: FilterPresenter = FilterPresenterImpl()
) {
    connectView(view, presenter)
}