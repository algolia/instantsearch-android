package com.algolia.instantsearch.demo.filter.range

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.algolia.instantsearch.core.number.Range
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.NumberRangeViewModel
import com.algolia.instantsearch.core.number.range.connectView
import com.algolia.instantsearch.demo.R
import com.algolia.instantsearch.demo.client
import com.algolia.instantsearch.demo.indexName
import com.algolia.instantsearch.demo.stubIndex
import com.algolia.instantsearch.helper.searcher.SearcherSingleIndex
import io.apptik.widget.MultiSlider
import kotlinx.android.synthetic.main.demo_filter_range.*


class FilterRangeDemo : AppCompatActivity() {
    private val searcher = SearcherSingleIndex(stubIndex)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.demo_filter_range)

        searcher.index = client.initIndex(intent.indexName)

        val viewModel = NumberRangeViewModel.Int(0..100)
        val sliderView = SliderRangeView(filterRangeSlider)
        val textView = object : NumberRangeView<Int> {
            override fun setItem(item: Range<Int>?) {
                filterRangeLabel.text = item.toString()
            }

            override var onClick: ((Range<Int>) -> Unit)? = null
        }
        viewModel.connectView(sliderView)
        viewModel.connectView(textView)

        viewModel.computeRange(Range.Int(0..9))

        searcher.searchAsync()
    }

    class SliderRangeView(private val sliderView: MultiSlider) : NumberRangeView<Int> {
        override fun setItem(item: Range<Int>?) {
            item?.let {
                println("set range")
                sliderView.min = it.min
                sliderView.max = it.max
            }
        }

        override var onClick: ((Range<Int>) -> Unit)? = null

        init {
            sliderView.setOnThumbValueChangeListener { multiSlider, _, _, _ ->
                println("Moved")
                onClick?.invoke(Range.Int(multiSlider.min..multiSlider.max))
            }
        }
    }
}