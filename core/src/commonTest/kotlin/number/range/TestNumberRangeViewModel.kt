package number.range

import com.algolia.instantsearch.core.number.Range
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import shouldBeTrue
import kotlin.test.Test


class TestNumberRangeViewModel {

    @Test
    fun onRangeChangeShouldCallOnRangeChanged() {
        val viewModel = NumberRangeViewModel.Int(Range.Int(0, 42))
        var onRangeCalled = false

        viewModel.onRangeChanged += { onRangeCalled = true }
        viewModel.range = Range.Int(0, 43)
        onRangeCalled.shouldBeTrue()
    }
}