package com.algolia.instantsearch.demo.filter.range

import com.algolia.instantsearch.core.number.Range
import com.algolia.instantsearch.core.number.range.NumberRangeView
import io.apptik.widget.MultiSlider


class RangeSliderView(private val slider: MultiSlider): NumberRangeView<Int> {

    private var item: Range<Int>? = null

    override var onClick: ((Range<Int>) -> Unit)? = null

    override fun setItem(item: Range<Int>?) {
        if (this.item != item) { // Avoid infinite loop through OnThumbValueChangeListener
            this.item = item
            item?.let {
                slider.getThumb(0).value = it.min
                slider.getThumb(1).value = it.max
            }
        }
    }

    override fun setBounds(bounds: Range<Int>?) {
        bounds?.let {
            slider.min = it.min
            slider.max = it.max
        }
    }

    init {
        slider.setOnThumbValueChangeListener { multiSlider, _, thumbIndex, value ->
            val valueMin = if (thumbIndex == 0) value else multiSlider.getThumb(0).value
            val valueMax = if (thumbIndex == 1) value else multiSlider.getThumb(1).value

            onClick?.invoke(Range.Int(valueMin..valueMax))
        }
    }
}