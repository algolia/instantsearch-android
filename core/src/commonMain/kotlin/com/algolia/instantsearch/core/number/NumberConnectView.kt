package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.connection.Connection


public fun <T> NumberViewModel<T>.connectView(
    view: NumberView<T>,
    connect: Boolean = true,
    presenter: NumberPresenter<T> = NumberPresenterImpl
): Connection where T : Number, T : Comparable<T> {
    return NumberConnectionView(this, view, presenter).apply { if (connect) connect() }
}