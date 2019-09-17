@file:JvmName("Number")

package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.connection.Connection
import kotlin.jvm.JvmName


/**
 * Connects this NumberViewModel to a NumberView, updating it when the viewModel's data changes.
 */
public fun <T> NumberViewModel<T>.connectView(
    view: NumberView<T>,
    presenter: NumberPresenter<T> = NumberPresenterImpl
): Connection where T : Number, T : Comparable<T> {
    return NumberConnectionView(this, view, presenter)
}