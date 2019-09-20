package com.algolia.instantsearch.helper.filter.numeric.comparison

import com.algolia.instantsearch.core.connection.Connection
import com.algolia.instantsearch.core.number.NumberPresenter
import com.algolia.instantsearch.core.number.NumberPresenterImpl
import com.algolia.instantsearch.core.number.NumberView
import com.algolia.instantsearch.core.number.connectView

/**
 * Connects this NumberViewModel to a NumberView,
 * updating it when the number or its bounds change.
 */
public fun <T> FilterComparisonConnector<T>.connectView(
    view: NumberView<T>,
    presenter: NumberPresenter<T> = NumberPresenterImpl
): Connection where T : Number, T : Comparable<T> {
    return viewModel.connectView(view, presenter)
}