package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.connection.autoConnect


public fun <T> NumberViewModel<T>.connectView(
    view: NumberView<T>,
    connect: Boolean = true,
    presenter: NumberPresenter<T> = NumberPresenterImpl
): Connection where T : Number, T : Comparable<T> {
    return NumberConnectionView(this, view, presenter).autoConnect(connect)
}