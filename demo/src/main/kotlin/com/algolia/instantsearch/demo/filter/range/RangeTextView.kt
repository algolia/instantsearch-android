package com.algolia.instantsearch.demo.filter.range

import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range


class RangeTextView(val view: TextView) : NumberRangeView<Int> {

    override var onRangeChanged: Callback<Range<Int>>? = null

    override fun setBounds(bounds: Range<Int>?) = Unit

    override fun setRange(range: Range<Int>?) {
        view.text = buildSpannedString {
            append("Range: ")
            if (range != null) {
                bold { append("${range.min}") }
                append(" to ")
                bold { append("${range.max}") }
            } else {
                italic { append("?") }
            }
        }
    }
}