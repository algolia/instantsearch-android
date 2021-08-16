package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.number.internal.NumberConnectionView

public fun <T> NumberViewModel<T>.connectView(
    view: NumberView<T>,
    presenter: NumberPresenter<T> = NumberPresenterImpl
): Connection where T : Number, T : Comparable<T> {
    return NumberConnectionView(this, view, presenter)
}
