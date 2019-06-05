package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.item.connectView


public fun <T : Number> NumberViewModel<T>.connectView(
    view: NumberView<T>,
    presenter: NumberPresenter<T> = NumberPresenterImpl
) {
    connectView(view, presenter)
    view.setComputation { computeNumber(it(item)) }
}