package com.algolia.instantsearch.demo.filter.range

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range
import io.apptik.widget.MultiSlider


class RangeSliderView(
    private val slider: MultiSlider
) : NumberRangeView<Int> {

    override var onRangeChanged: Callback<Range<Int>>? = null

    private var range: Range<Int>? = null
    private var bounds: Range<Int>? = null
    private val changeListener: (multiSlider: MultiSlider, thumb: MultiSlider.Thumb, thumbIndex: Int, value: Int) -> Unit =
        { multiSlider, _, thumbIndex, value ->
            val valueMin = if (thumbIndex == 0) value else multiSlider.getThumb(0).value
            val valueMax = if (thumbIndex == 1) value else multiSlider.getThumb(1).value
            if (range?.min != valueMin || range?.max != valueMax) {
                onRangeChanged?.invoke(Range(valueMin..valueMax))
            }
        }

    init {
        slider.setOnThumbValueChangeListener(changeListener)
    }

    override fun setRange(range: Range<Int>?) {
        slider.setOnThumbValueChangeListener(null)
        if (range == null) {
            slider.getThumb(0).unsetThumbValue()
            slider.getThumb(1).unsetThumbValue(true)
        } else if (this.range != range) { // Avoid infinite loop through OnThumbValueChangeListener
            slider.getThumb(0).setThumbValue(range.min)
            slider.getThumb(1).setThumbValue(range.max)
        }
        this.range = range
        slider.setOnThumbValueChangeListener(changeListener)
    }

    override fun setBounds(bounds: Range<Int>?) {
        bounds?.let {
            this.bounds = it
            slider.setMin(it.min, true, false)
            slider.setMax(it.max, true, false)
            slider.getThumb(0).value = range?.min ?: it.min
            slider.getThumb(1).value = range?.max ?: it.max
        }
    }

    private fun MultiSlider.Thumb.setThumbValue(value: Int) {
        this.value = value
    }

    private fun MultiSlider.Thumb.unsetThumbValue(isMax: Boolean = false) {
        bounds?.let { value = if (isMax) it.max else it.min }
    }
}