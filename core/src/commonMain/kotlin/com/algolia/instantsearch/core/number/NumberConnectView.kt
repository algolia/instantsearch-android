package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.item.connectView
import com.algolia.instantsearch.core.observable.ObservableKey


public fun <T> NumberViewModel<T>.connectView(
    view: NumberView<T>,
    key: ObservableKey? = null,
    presenter: NumberPresenter<T> = NumberPresenterImpl
) where T : Number, T : Comparable<T> {
    number.connectView(view, key, presenter)
    view.setComputation { coerce(it(number.get())) }
}