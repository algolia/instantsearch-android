package com.algolia.instantsearch.demo.filter.range

import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import com.algolia.instantsearch.core.number.Range
import com.algolia.instantsearch.core.number.range.NumberRangeView


class RangeTextView(val view: TextView) : NumberRangeView<Int> {

    private var bounds: Range<Int>? = null
    private var item: Range<Int>? = null

    override var onClick: ((Range<Int>) -> Unit)? = null

    override fun setBounds(bounds: Range<Int>?) {
        this.bounds = bounds
        updateText()
    }

    override fun setItem(item: Range<Int>?) {
        this.item = item
        updateText()
    }

    private fun updateText() {
        view.text = buildSpannedString {
            bounds?.let { append("[${it.min}| ") }
            item?.let { bold { append("${it.min} TO ${it.max}") } }
            bounds?.let { append(" |${it.max}]") }
        }
    }
}