package com.algolia.instantsearch.demo.filter.range

import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.italic
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range


class RangeTextView(val view: TextView) : NumberRangeView<Int> {

    override var onClick: ((Range<Int>) -> Unit)? = null

    override fun setBounds(bounds: Range<Int>?) = Unit

    override fun setItem(item: Range<Int>?) {
        view.text = buildSpannedString {
            append("Range: ")
            if (item != null) {
                bold { append("${item.min}")}
                append(" to ")
                bold { append("${item.max}") }
            } else {
                italic { append("?") }
            }
        }
    }
}