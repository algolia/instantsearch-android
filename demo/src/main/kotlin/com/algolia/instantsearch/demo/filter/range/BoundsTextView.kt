package com.algolia.instantsearch.demo.filter.range

import android.widget.TextView
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range


class BoundsTextView(val view: TextView) : NumberRangeView<Int> {

    override var onRangeChanged: Callback<Range<Int>>? = null

    private var bounds: Range<Int>? = null

    override fun setBounds(bounds: Range<Int>?) {
        this.bounds = bounds
        view.text = bounds?.let {
            "Bounds: ${it.min} to ${it.max}"
        } ?: "No bounds"
    }

    override fun setRange(range: Range<Int>?) = Unit
}