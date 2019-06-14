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

        override fun setItem(item: Range<Int>?) {
            range = item
        }

        override var onClick: ((Range<Int>) -> Unit)? = null
    }

    @Test
    fun connectShouldCallSetItem() {
        val viewModel = NumberRangeViewModel.Int()
        val view = MockNumberRangeView()

        viewModel.range = Range.Int(0..20)
        viewModel.connectView(view)
        view.range shouldEqual viewModel.range
    }

    @Test
    fun onRangeChangedShouldUpdateRange() {
        val viewModel = NumberRangeViewModel.Int()
        val view = MockNumberRangeView()

        viewModel.connectView(view)
        viewModel.range = Range.Int(0..20)
        view.range shouldEqual viewModel.range
    }
}