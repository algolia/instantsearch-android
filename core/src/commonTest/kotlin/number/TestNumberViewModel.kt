package number

import com.algolia.instantsearch.core.number.NumberViewModel
import com.algolia.instantsearch.core.number.Range
import shouldEqual
import kotlin.test.Test


class TestNumberViewModel  {

    @Test
    fun computeShouldNotCoerceIfNoBounds() {
        val viewModel = NumberViewModel.Int()
        val value = -1

        viewModel.onNumberComputed += { viewModel.item = it }
        viewModel.computeNumber(value)
        viewModel.item shouldEqual value
    }

    @Test
    fun computeShouldCoerceMin() {
        val range = 0 .. 10
        val viewModel = NumberViewModel.Int(range)

        viewModel.onNumberComputed += { viewModel.item = it }
        viewModel.computeNumber(-1)
        viewModel.item shouldEqual range.first
    }

    @Test
    fun computeShouldCoerceMax() {
        val range = 0 .. 10
        val viewModel = NumberViewModel.Int(range)

        viewModel.onNumberComputed += { viewModel.item = it }
        viewModel.computeNumber(11)
        viewModel.item shouldEqual range.last
    }

    @Test
    fun changeMinShouldCoerceNumber() {
        val range = 0 .. 10
        val viewModel = NumberViewModel.Int(range)
        val value = 5

        viewModel.onNumberComputed += { viewModel.item = it }
        viewModel.computeNumber(value)
        viewModel.item shouldEqual value
        viewModel.applyBounds(Range.Int(6 .. 10))
        viewModel.item shouldEqual 6
    }

    @Test
    fun changeMaxShouldCoerceNumber() {
        val range = 0 until 10
        val viewModel = NumberViewModel.Int(range)
        val value = 5

        viewModel.onNumberComputed += { viewModel.item = it }
        viewModel.computeNumber(value)
        viewModel.item shouldEqual value
        viewModel.applyBounds(Range.Int(0 .. 4))
        viewModel.item shouldEqual 4
    }
}