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

    init {
        slider.setOnThumbValueChangeListener(changeListener)
    }

    override var onClick: ((Range<Int>) -> Unit)? = null

    override fun setItem(item: Range<Int>?) {
        slider.setOnThumbValueChangeListener(null)
        if (item == null) {
            slider.getThumb(0).unsetThumbValue()
            slider.getThumb(1).unsetThumbValue(true)
        } else if (this.item != item) { // Avoid infinite loop through OnThumbValueChangeListener
            slider.getThumb(0).setThumbValue(item.min)
            slider.getThumb(1).setThumbValue(item.max)
        }
        this.item = item
        slider.setOnThumbValueChangeListener(changeListener)
    }

    override fun setBounds(bounds: Range<Int>?) {
        bounds?.let {
            this.bounds = it
            slider.setMin(it.min, true, false)
            slider.setMax(it.max, true, false)
            slider.getThumb(0).value = item?.min ?: it.min
            slider.getThumb(1).value = item?.max ?: it.max
        }
    }

    private fun MultiSlider.Thumb.setThumbValue(value: Int) {
        this.value = value
        thumb.setTintList(null)
        range.setTint(slider.resources.getColor(R.color.blue_dark, slider.context.theme))
    }

    private fun MultiSlider.Thumb.unsetThumbValue(isMax: Boolean = false) {
        thumb.setTint(slider.resources.getColor(R.color.blue_dark_a25, slider.context.theme))
        range.setTint(slider.resources.getColor(R.color.blue_dark_a25, slider.context.theme))
        bounds?.let { value = if (isMax) it.max else it.min }
    }
}