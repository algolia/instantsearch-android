package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.connection.Connection


public class NumberConnectionView<T>(
    public val viewModel: NumberViewModel<T>,
    public val view: NumberView<T>,
    public val presenter: NumberPresenter<T>
) : Connection where T : Number, T : Comparable<T> {

    override var isConnected: Boolean = false

    private val updateText: (T?) -> Unit = { number ->
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