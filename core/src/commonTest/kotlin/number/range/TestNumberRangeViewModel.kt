package number.range

import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.core.number.range.Range
import shouldEqual
import kotlin.test.Test


class TestNumberRangeViewModel {

    @Test
    fun computeShouldNotCoerceIfNoBounds() {
        val viewModel = NumberRangeViewModel<Int>()
        val range = Range(1..5)

        viewModel.onRangeComputed += { viewModel.item = it }
        viewModel.computeRange(range)
        viewModel.item shouldEqual range
    }

    @Test
    fun computeShouldCoerceMin() {
        val bounds = Range(0f..10f)
        val range = Range(-1f..9f)
        val viewModel = NumberRangeViewModel(bounds)

        viewModel.onRangeComputed += { viewModel.item = it }
        viewModel.computeRange(range)
        viewModel.item shouldEqual Range(bounds.min, range.max)
    }

    @Test
    fun computeShouldCoerceMax() {
        val bounds = Range(0L..10L)
        val range = Range(0L..11L)
        val viewModel = NumberRangeViewModel(bounds)

        viewModel.onRangeComputed += { viewModel.item = it }
        viewModel.computeRange(range)
        viewModel.item shouldEqual Range(range.min, bounds.max)
    }

    @Test
    fun changeMinShouldCoerceRange() {
        val viewModel = NumberRangeViewModel(Range(0.5..10.5))
        val range = Range(1.5..9.5)

        viewModel.onRangeComputed += { viewModel.item = it }
        viewModel.computeRange(range)
        viewModel.item shouldEqual range
        viewModel.bounds = Range(6.5..10.5)
        viewModel.item shouldEqual Range(6.5..9.5)
    }

    @Test
    fun changeMaxShouldCoerceRange() {
        val viewModel = NumberRangeViewModel(Range(0..10))
        val range = Range(1..9)

        viewModel.onRangeComputed += { viewModel.item = it }
        viewModel.computeRange(range)
        viewModel.item shouldEqual range
        viewModel.bounds = Range(0..8)
        viewModel.item shouldEqual Range(1..8)
    }
}