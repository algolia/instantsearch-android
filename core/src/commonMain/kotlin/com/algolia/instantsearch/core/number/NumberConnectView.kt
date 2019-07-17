package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.item.connectView


public fun <T> NumberViewModel<T>.connectView(
    view: NumberView<T>,
    presenter: NumberPresenter<T> = NumberPresenterImpl
) where T : Number, T : Comparable<T> {
    number.connectView(view, presenter)
    view.setComputation { coerce(it(number.get())) }
}