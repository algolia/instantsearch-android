package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.connection.ConnectionImpl
import kotlin.jvm.JvmOverloads


/**
 * A connection between a NumberViewModel and a NumberView,
 * updating the view when the viewModel's data changes.
 */
internal data class NumberConnectionView<T> @JvmOverloads constructor(
    private val viewModel: NumberViewModel<T>,
    private val view: NumberView<T>,
    private val presenter: NumberPresenter<T> = NumberPresenterImpl
) : ConnectionImpl() where T : Number, T : Comparable<T> {

    private val updateText: Callback<T?> = { number ->
        view.setText(presenter(number))
    }

    override fun connect() {
        super.connect()
        viewModel.number.subscribePast(updateText)
        view.setComputation { computation -> viewModel.coerce(computation(viewModel.number.value)) }
    }

    override fun disconnect() {
        super.disconnect()
        viewModel.number.unsubscribe(updateText)
        view.setComputation { }
    }
}