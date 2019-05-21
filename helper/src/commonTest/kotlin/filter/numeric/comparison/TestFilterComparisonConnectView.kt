package filter.numeric.comparison

import com.algolia.instantsearch.core.selectable.number.SelectableNumberView
import com.algolia.instantsearch.core.selectable.number.SelectableNumberViewModel
import com.algolia.instantsearch.core.selectable.number.decrement
import com.algolia.instantsearch.core.selectable.number.increment
import com.algolia.instantsearch.helper.filter.numeric.comparison.connectView
import shouldEqual
import kotlin.test.Test


class TestFilterComparisonConnectView {

    private class MockSelectableNumberView : SelectableNumberView<Int> {

        var onClickIncrement: (() -> Unit)? = null
        var onClickDecrement: (() -> Unit)? = null

        var int: Int? = null

        override fun setComputation(computation: ((Int?) -> Int?) -> Unit) {
            onClickIncrement = {
                computation.increment()
            }
            onClickDecrement = {
                computation.decrement()
            }
        }

        override fun setNumber(number: Int?) {
            int = number
        }
    }

    @Test
    fun connectShouldCallSetSelectedAndSetItems() {
        val view = MockSelectableNumberView()
        val viewModel = SelectableNumberViewModel.Int(0 .. 10)

        viewModel.number = 5
        viewModel.connectView(view)
        view.int shouldEqual 5
    }

    @Test
    fun onClickShouldCallOnSelectionsComputed() {
        val view = MockSelectableNumberView()
        val viewModel = SelectableNumberViewModel.Int(0 .. 10)

        viewModel.onNumberComputed += { viewModel.number = it }
        viewModel.connectView(view)
        view.onClickIncrement!!()
        view.int shouldEqual 0
        view.onClickIncrement!!()
        view.int shouldEqual 1
        view.onClickDecrement!!()
        view.int shouldEqual 0
    }

    @Test
    fun onSelectedChangedShouldCallSetSelected() {
        val view = MockSelectableNumberView()
        val viewModel = SelectableNumberViewModel.Int(0 .. 10)

        viewModel.connectView(view)
        viewModel.number = 5
        view.int shouldEqual 5
    }
}