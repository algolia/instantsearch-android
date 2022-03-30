package com.algolia.instantsearch.showcase.filter.range

import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range
import com.google.android.material.slider.RangeSlider

class RangeSliderView(
    private val slider: RangeSlider
) : NumberRangeView<Int> {

    override var onRangeChanged: Callback<Range<Int>>? = null

    init {
        slider.addOnChangeListener { slider, _, _ ->
            onRangeChanged?.invoke(slider.values.toRange())
        }
    }

    override fun setRange(range: Range<Int>?) {
        range?.let {
            slider.values = range.toValues()
        }
    }

    override fun setBounds(bounds: Range<Int>?) {
        bounds?.let {
            slider.valueFrom = bounds.min.toFloat()
            slider.valueTo = bounds.max.toFloat()
        }
    }

    private fun List<Float>.toRange(): Range<Int> = Range(get(0).toInt(), get(1).toInt())

    private fun Range<Int>.toValues(): List<Float> = listOf(min.toFloat(), max.toFloat())
}
