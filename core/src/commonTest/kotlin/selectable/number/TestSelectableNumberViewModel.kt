package selectable.number

import com.algolia.instantsearch.core.selectable.number.SelectableNumberViewModel
import com.algolia.instantsearch.core.selectable.range.Range
import shouldEqual
import kotlin.test.Test


class TestSelectableNumberViewModel  {

    @Test
    fun computeShouldNotCoerceIfNoBounds() {
        val viewModel = SelectableNumberViewModel.Int()
        val value = -1

        viewModel.onNumberComputed += { viewModel.number = it }
        viewModel.computeNumber(value)
        viewModel.number shouldEqual value
    }

    @Test
    fun computeShouldCoerceMin() {
        val range = 0 .. 10
        val viewModel = SelectableNumberViewModel.Int(range)

        viewModel.onNumberComputed += { viewModel.number = it }
        viewModel.computeNumber(-1)
        viewModel.number shouldEqual range.first
    }

    @Test
    fun computeShouldCoerceMax() {
        val range = 0 .. 10
        val viewModel = SelectableNumberViewModel.Int(range)

        viewModel.onNumberComputed += { viewModel.number = it }
        viewModel.computeNumber(11)
        viewModel.number shouldEqual range.last
    }

    @Test
    fun changeMinShouldCoerceNumber() {
        val range = 0 .. 10
        val viewModel = SelectableNumberViewModel.Int(range)
        val value = 5

        viewModel.onNumberComputed += { viewModel.number = it }
        viewModel.computeNumber(value)
        viewModel.number shouldEqual value
        viewModel.computeBounds(Range.Int(6 .. 10))
        viewModel.number shouldEqual 6
    }

    @Test
    fun changeMaxShouldCoerceNumber() {
        val range = 0 until 10
        val viewModel = SelectableNumberViewModel.Int(range)
        val value = 5

        viewModel.onNumberComputed += { viewModel.number = it }
        viewModel.computeNumber(value)
        viewModel.number shouldEqual value
        viewModel.computeBounds(Range.Int(0 .. 4))
        viewModel.number shouldEqual 4
    }
}