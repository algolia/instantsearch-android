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

        override fun setItem(item: String) {
            string = item
        }
    }

    @Test
    fun connectShouldCallSetItem() {
        val view = MockNumberView()
        val viewModel = NumberViewModel.Int(0 .. 10)

        viewModel.item = 5
        viewModel.connectView(view)
        view.string shouldEqual "5"
    }

    @Test
    fun onItemChangedShouldCallSetItem() {
        val view = MockNumberView()
        val viewModel = NumberViewModel.Int(0 .. 10)

        viewModel.connectView(view)
        viewModel.item = 5
        view.string shouldEqual "5"
    }

    @Test
    fun onClickShouldCallOnNumberComputed() {
        val view = MockNumberView()
        val viewModel = NumberViewModel.Int(0 .. 10)

        viewModel.onNumberComputed += { viewModel.item = it }
        viewModel.connectView(view)
        view.onClickIncrement!!()
        view.string shouldEqual "0"
        view.onClickIncrement!!()
        view.string shouldEqual "1"
        view.onClickDecrement!!()
        view.string shouldEqual "0"
    }
}