package number.range

import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.connectView
import shouldEqual
import kotlin.test.Test


class TestNumberRangeConnectView {

    private class MockNumberRangeView : NumberRangeView<Int> {

        public var range: Range<Int>? = null
        public var bounds: Range<Int>? = null
            private set

        override fun setItem(item: Range<Int>?) {
            range = item
        }

        override fun setBounds(bounds: Range<Int>?) {
            this.bounds = bounds
        }

        override var onClick: ((Range<Int>) -> Unit)? = null
    }

    @Test
    fun connectShouldCallSetItem() {
        val viewModel = NumberRangeViewModel<Int>()
        val view = MockNumberRangeView()

        viewModel.item = Range(0..20)
        viewModel.connectView(view)
        view.range shouldEqual viewModel.item
    }

    @Test
    fun connectShouldCallSetBounds() {
        val viewModel = NumberRangeViewModel.Int()
        val view = MockNumberRangeView()

        viewModel.bounds = Range.Int(0..20)
        viewModel.connectView(view)
        view.bounds shouldEqual viewModel.bounds
    }

    @Test
    fun onRangeChangedShouldUpdateRange() {
        val viewModel = NumberRangeViewModel<Int>()
        val view = MockNumberRangeView()

        viewModel.connectView(view)
        viewModel.item = Range(0..20)
        view.range shouldEqual viewModel.item
    }

    @Test
    fun onBoundsComputedShouldUpdateBounds() {
        val viewModel = NumberRangeViewModel.Int()
        val view = MockNumberRangeView()

        viewModel.connectView(view)
        viewModel.bounds = Range.Int(0..20)
        view.bounds shouldEqual viewModel.bounds
    }
}