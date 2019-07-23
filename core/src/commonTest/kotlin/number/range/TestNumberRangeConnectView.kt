package number.range

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.connectionView
import shouldEqual
import kotlin.test.Test


class TestNumberRangeConnectView {

    private class MockNumberRangeView : NumberRangeView<Int> {

        public var rangeInt: Range<Int>? = null
        public var bounds: Range<Int>? = null
            private set

        override var onRangeChanged: Callback<Range<Int>>? = null

        override fun setRange(range: Range<Int>?) {
            this.rangeInt = range
        }


        override fun setBounds(bounds: Range<Int>?) {
            this.bounds = bounds
        }
    }

    @Test
    fun connectShouldCallSetItem() {
        val viewModel = NumberRangeViewModel<Int>()
        val view = MockNumberRangeView()
        val connection = viewModel.connectionView(view)

        viewModel.range.value = Range(0..20)
        connection.connect()
        view.rangeInt shouldEqual viewModel.range.value
    }

    @Test
    fun connectShouldCallSetBounds() {
        val viewModel = NumberRangeViewModel<Int>()
        val view = MockNumberRangeView()
        val connection = viewModel.connectionView(view)

        viewModel.bounds.value = Range(0..20)
        connection.connect()
        view.bounds shouldEqual viewModel.bounds.value
    }

    @Test
    fun onRangeChangedShouldUpdateRange() {
        val viewModel = NumberRangeViewModel<Int>()
        val view = MockNumberRangeView()
        val connection = viewModel.connectionView(view)

        connection.connect()
        viewModel.range.value = Range(0..20)
        view.rangeInt shouldEqual viewModel.range.value
    }

    @Test
    fun onBoundsComputedShouldUpdateBounds() {
        val viewModel = NumberRangeViewModel<Int>()
        val view = MockNumberRangeView()
        val connection = viewModel.connectionView(view)

        connection.connect()
        viewModel.bounds.value = Range(0..20)
        view.bounds shouldEqual viewModel.bounds.value
    }
}