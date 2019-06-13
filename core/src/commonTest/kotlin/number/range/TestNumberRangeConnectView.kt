package number.range

import com.algolia.instantsearch.core.number.Range
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.core.number.range.connectView
import shouldEqual
import kotlin.test.Test

class TestNumberRangeConnectView {

    private class MockNumberRangeView : NumberRangeView<Int> {
        public var range: Range<Int>? = null

        override fun setItem(item: Range<Int>) {
            range = item
        }

        override var onNewRange: ((Range<Int>) -> Unit)? = null
    }

    @Test
    fun connectShouldCallSetRange() {
        val viewModel = NumberRangeViewModel.Int(0..10)
        val view = MockNumberRangeView()

        viewModel.connectView(view)
        view.range shouldEqual Range.Int(0, 10)
    }

    @Test
    fun onRangeChangedShouldUpdateRange() {
        val viewModel = NumberRangeViewModel.Int(0..10)
        val view = MockNumberRangeView()

        viewModel.connectView(view)
        viewModel.range = Range.Int(0..20)
        view.range shouldEqual Range.Int(0, 20)
    }

    @Test
    fun onNewRangeShouldCallOnRangeChanged() {
        val viewModel = NumberRangeViewModel.Int(0..10)
        val view = MockNumberRangeView()
        var currentRange: Range<Int>? = null

        viewModel.onItemChanged += { currentRange = it }
        viewModel.connectView(view)
        view.onNewRange!!(Range.Int(0..20))
        currentRange shouldEqual Range.Int(0, 20)
    }
}