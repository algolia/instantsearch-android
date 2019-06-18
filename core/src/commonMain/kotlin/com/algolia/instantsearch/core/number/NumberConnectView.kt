package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.item.connectView


public fun <T> NumberViewModel<T>.connectView(
    view: NumberView<T>,
    presenter: NumberPresenter<T> = NumberPresenterImpl
) where T : Number, T : Comparable<T> {
    connectView(view, presenter)
    view.setComputation { computeNumber(it(item)) }
}