@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.number.range

import com.algolia.instantsearch.compose.number.range.internal.NumberRangeStateImpl
import com.algolia.instantsearch.core.number.range.NumberRangeView
import com.algolia.instantsearch.core.number.range.Range

/**
 * [NumberRangeView] for compose.
 */
public interface NumberRangeState<T> : NumberRangeView<T> where T : Number, T : Comparable<T> {
    /**
     * Range number values.
     */
    public val range: Range<T>?

    /**
     * Bounds number values.
     */
    public val bounds: Range<T>?
}

/**
 * Creates an instance of [NumberRangeState].
 *
 * @param range initial range value
 * @param bounds initial bounds value
 */
public fun <T> NumberRangeState(range: Range<T>? = null, bounds: Range<T>? = null): NumberRangeState<T> where T : Number, T : Comparable<T> {
    return NumberRangeStateImpl(range, bounds)
}
