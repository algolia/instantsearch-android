package number.range

import com.algolia.instantsearch.core.number.Range
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import shouldEqual
import kotlin.test.Test


class TestNumberRangeViewModel {

    @Test
    fun computeShouldNotCoerceIfNoBounds() {
        val viewModel = NumberRangeViewModel.Int()
        val range = Range.Int(1..5)

        viewModel.onRangeComputed += { viewModel.item = it }
        viewModel.computeRange(range)
        viewModel.item shouldEqual range
    }

    @Test
    fun computeShouldCoerceMin() {
        val bounds = Range.Float(0f..10f)
        val range = Range.Float(-1f..9f)
        val viewModel = NumberRangeViewModel.Float(bounds = bounds)

        viewModel.onRangeComputed += { viewModel.item = it }
        viewModel.computeRange(range)
        viewModel.item shouldEqual Range.Float(bounds.min, range.max)
    }

    @Test
    fun computeShouldCoerceMax() {
        val bounds = Range.Long(0L..10L)
        val range = Range.Long(0L..11L)
        val viewModel = NumberRangeViewModel.Long(bounds)

        viewModel.onRangeComputed += { viewModel.item = it }
        viewModel.computeRange(range)
        viewModel.item shouldEqual Range.Long(range.min, bounds.max)
    }

    @Test
    fun changeMinShouldCoerceRange() {
        val viewModel = NumberRangeViewModel.Double(Range.Double(0.5..10.5))
        val range = Range.Double(1.5..9.5)

        viewModel.onRangeComputed += { viewModel.item = it }
        viewModel.computeRange(range)
        viewModel.item shouldEqual range
        viewModel.bounds = Range.Double(6.5..10.5)
        viewModel.item shouldEqual Range.Double(6.5..9.5)
    }

    @Test
    fun changeMaxShouldCoerceRange() {
        val viewModel = NumberRangeViewModel.Int(Range.Int(0..10))
        val range = Range.Int(1..9)

        viewModel.onRangeComputed += { viewModel.item = it }
        viewModel.computeRange(range)
        viewModel.item shouldEqual range
        viewModel.bounds = Range.Int(0..8)
        viewModel.item shouldEqual Range.Int(1..8)
    }
}