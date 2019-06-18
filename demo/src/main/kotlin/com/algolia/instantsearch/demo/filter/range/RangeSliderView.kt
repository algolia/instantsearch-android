package com.algolia.instantsearch.demo.filter.range

import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range
import com.algolia.instantsearch.demo.R
import io.apptik.widget.MultiSlider


class RangeSliderView(
    private val slider: MultiSlider
) : NumberRangeView<Int> {

    private var item: Range<Int>? = null
    private var bounds: Range<Int>? = null

    private val changeListener: (multiSlider: MultiSlider, thumb: MultiSlider.Thumb, thumbIndex: Int, value: Int) -> Unit =
        { multiSlider, _, thumbIndex, value ->
            val valueMin = if (thumbIndex == 0) value else multiSlider.getThumb(0).value
            val valueMax = if (thumbIndex == 1) value else multiSlider.getThumb(1).value
            if (item?.min != valueMin || item?.max != valueMax) {
                onClick?.invoke(Range(valueMin..valueMax))
            }
        }

    override var onClick: ((Range<Int>) -> Unit)? = null

    override fun setItem(item: Range<Int>?) {
        slider.setOnThumbValueChangeListener(null)
        if (item == null) {
            slider.unsetThumbValues()
        } else if (this.item != item) { // Avoid infinite loop through OnThumbValueChangeListener
            this.item = item
            slider.setThumbValues(item)
        }
        slider.setOnThumbValueChangeListener(changeListener)
    }

    private fun MultiSlider.setThumbValues(value: Range<Int>) {
        getThumb(0).apply { setThumbValue(value.min) }
        getThumb(1).apply { setThumbValue(value.max) }
    }

    private fun MultiSlider.Thumb.setThumbValue(value: Int) {
        this.value = value
        thumb.setTintList(null)
    }

    private fun MultiSlider.unsetThumbValues() {
        getThumb(0).apply { unsetThumbValue() }
        getThumb(1).apply { unsetThumbValue(true) }
    }

    private fun MultiSlider.Thumb.unsetThumbValue(isMax: Boolean = false) {
        thumb.setTint(slider.resources.getColor(R.color.blue_dark_a25, slider.context.theme))
        bounds?.let { value = if (isMax) it.max else it.min }
    }

    override fun setBounds(bounds: Range<Int>?) {
        bounds?.let {
            this.bounds = bounds
            slider.min = it.min
            slider.max = it.max
        }
    }

    init {
        slider.setOnThumbValueChangeListener(changeListener)
    }
}