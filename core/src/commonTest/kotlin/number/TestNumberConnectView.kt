package number

import com.algolia.instantsearch.core.number.*
import shouldEqual
import kotlin.test.Test


class TestNumberConnectView {

    private class MockNumberView : NumberView<Int> {

        var onClickIncrement: (() -> Unit)? = null
        var onClickDecrement: (() -> Unit)? = null

        var string: String? = null

        override fun setComputation(computation: ((Int?) -> Int?) -> Unit) {
            onClickIncrement = {
                computation.increment()
            }
            onClickDecrement = {
                computation.decrement()
            }
        }

        override fun setText(text: String) {
            string = text
        }
    }

    @Test
    fun connectShouldCallSetItem() {
        val view = MockNumberView()
        val viewModel = NumberViewModel(0..10)
        val connection = viewModel.connectionView(view)

        viewModel.number.value = 5
        connection.connect()
        view.string shouldEqual "5"
    }

    @Test
    fun onSetNumberShouldCallSetItem() {
        val view = MockNumberView()
        val viewModel = NumberViewModel(0..10)
        val connection = viewModel.connectionView(view)

        connection.connect()
        viewModel.number.value = 5
        view.string shouldEqual "5"
    }

    @Test
    fun onClickShouldCallEventSubscription() {
        val view = MockNumberView()
        val viewModel = NumberViewModel(0..10)
        val connection = viewModel.connectionView(view)

        viewModel.eventNumber.subscribe { viewModel.number.value = it }
        connection.connect()
        view.onClickIncrement!!()
        view.string shouldEqual "0"
        view.onClickIncrement!!()
        view.string shouldEqual "1"
        view.onClickDecrement!!()
        view.string shouldEqual "0"
    }
}