package number.range

import com.algolia.instantsearch.core.event.Event
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.core.number.range.connectView
import shouldEqual
import kotlin.test.Test


class TestNumberRangeConnectView {

    private class MockNumberRangeView : NumberRangeView<Int> {

        public var rangeInt: Range<Int>? = null
        public var bounds: Range<Int>? = null
            private set

        override var onRangeChanged: Event<Range<Int>>? = null

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

        viewModel.range.set(Range(0..20))
        viewModel.connectView(view)
        view.rangeInt shouldEqual viewModel.range.get()
    }

    @Test
    fun connectShouldCallSetBounds() {
        val viewModel = NumberRangeViewModel<Int>()
        val view = MockNumberRangeView()

        viewModel.bounds.set(Range(0..20))
        viewModel.connectView(view)
        view.bounds shouldEqual viewModel.bounds.get()
    }

    @Test
    fun onRangeChangedShouldUpdateRange() {
        val viewModel = NumberRangeViewModel<Int>()
        val view = MockNumberRangeView()

        viewModel.connectView(view)
        viewModel.range.set(Range(0..20))
        view.rangeInt shouldEqual viewModel.range.get()
    }

    @Test
    fun onBoundsComputedShouldUpdateBounds() {
        val viewModel = NumberRangeViewModel<Int>()
        val view = MockNumberRangeView()

        viewModel.connectView(view)
        viewModel.bounds.set(Range(0..20))
        view.bounds shouldEqual viewModel.bounds.get()
    }
}