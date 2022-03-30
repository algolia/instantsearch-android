package com.algolia.instantsearch.showcase.filter.rating

import android.widget.TextView
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range

class RatingTextView(private val textView: TextView) : NumberRangeView<Float> {

    override var onRangeChanged: Callback<Range<Float>>? = null

    override fun setBounds(bounds: Range<Float>?) = Unit

    override fun setRange(range: Range<Float>?) {
        textView.text = "${range?.min ?: 0}"
    }
}
