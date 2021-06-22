package com.algolia.instantsearch.compose.number.range.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.number.range.NumberRangeState
import com.algolia.instantsearch.core.Callback
import com.algolia.instantsearch.core.number.range.Range

/**
 * Implementation of [NumberRangeState].
 */
internal class NumberRangeStateImpl<T>(
    range: Range<T>?,
    bounds: Range<T>?
) : NumberRangeState<T> where T : Number, T : Comparable<T> {

    @set:JvmName("_range")
    override var range: Range<T>? by mutableStateOf(range)

    @set:JvmName("_bounds")
    override var bounds: Range<T>? by mutableStateOf(bounds)

    override var onRangeChanged: Callback<Range<T>>? = null

    override fun setRange(range: Range<T>?) {
        this.range = range
    }

    override fun setBounds(bounds: Range<T>?) {
        this.bounds = bounds
    }
}
