package com.algolia.instantsearch.demo.filter.range

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.buildSpannedString
import com.algolia.instantsearch.core.number.Range
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.core.number.range.connectView
import com.algolia.instantsearch.demo.*
import com.algolia.instantsearch.helper.filter.range.connectFilterState
import com.algolia.instantsearch.helper.filter.state.FilterState
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import com.algolia.instantsearch.helper.searcher.connectFilterState
import com.algolia.search.model.Attribute
import io.apptik.widget.MultiSlider
import kotlinx.android.synthetic.main.demo_filter_range.*
import kotlinx.android.synthetic.main.header_filter.*


class FilterRangeDemo : AppCompatActivity() {
    private val searcher = SearcherSingleIndex(stubIndex)
    private val filterState = FilterState()
    private val price = Attribute("price")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_range)

        searcher.connectFilterState(filterState)

        val viewModel = NumberRangeViewModel.Int(0..100)
        val sliderView = SliderRangeView(filterRangeSlider)
        val textView = object : NumberRangeView<Int> {
            private var bounds: Range<Int>? = null
            private var item: Range<Int>? = null

            override fun setBounds(bounds: Range<Int>?) {
                this.bounds = bounds
                updateText()
            }

            override fun setItem(item: Range<Int>?) {
                this.item = item
                updateText()
            }

            fun updateText() {
                filterRangeLabel.text = buildSpannedString {
                    bounds?.let {
                        append("{${it.min}")
                    }
                    append("[")
                    item?.let {
                        append("${it.min} TO ${it.max}")
                    }
                    append("]")
                    bounds?.let {
                        append("${it.max}}")
                    }
                }
            }

            override var onClick: ((Range<Int>) -> Unit)? = null
        }
        viewModel.connectView(sliderView)
        viewModel.connectView(textView)
        viewModel.connectFilterState(price, filterState)
        viewModel.computeRange(Range.Int(1..9))

        configureToolbar(toolbar)
        configureSearcher(searcher)
        onFilterChangedThenUpdateFiltersText(filterState, filtersTextView, price)
        onClearAllThenClearFilters(filterState, filtersClearAll)
        onErrorThenUpdateFiltersText(searcher, filtersTextView)
        onResponseChangedThenUpdateNbHits(searcher, nbHits)

        searcher.searchAsync()
    }

    class SliderRangeView(private val sliderView: MultiSlider) : NumberRangeView<Int> {

        private var item: Range<Int>? = null

        override fun setItem(item: Range<Int>?) {
            if (this.item != item) {
                this.item = item
                item?.let {
                    sliderView.getThumb(0).value = it.min
                    sliderView.getThumb(1).value = it.max
                }
            }
        }

        override fun setBounds(bounds: Range<Int>?) {
            bounds?.let {
                sliderView.min = it.min
                sliderView.max = it.max
            }
        }

        override var onClick: ((Range<Int>) -> Unit)? = null

        init {
            sliderView.setOnThumbValueChangeListener { multiSlider, _, thumbIndex, value ->
                val valueMin = if (thumbIndex == 0) value else multiSlider.getThumb(0).value
                val valueMax = if (thumbIndex == 1) value else multiSlider.getThumb(1).value

                onClick?.invoke(Range.Int(valueMin..valueMax))
            }
        }
    }
}