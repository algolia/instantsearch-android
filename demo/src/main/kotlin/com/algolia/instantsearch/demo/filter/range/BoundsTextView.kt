package com.algolia.instantsearch.demo.filter.range

import android.widget.TextView
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range


class BoundsTextView(val view: TextView) : NumberRangeView<Int> {

    private var bounds: Range<Int>? = null

    override var onClick: ((Range<Int>) -> Unit)? = null

    override fun setBounds(bounds: Range<Int>?) {
        this.bounds = bounds
        view.text = bounds?.let {
            "Bounds: ${it.min} to ${it.max}"
        } ?: "No bounds"
    }

    override fun setItem(item: Range<Int>?) = Unit
}