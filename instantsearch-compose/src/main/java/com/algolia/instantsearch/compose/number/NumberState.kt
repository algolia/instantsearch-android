@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.number

import com.algolia.instantsearch.compose.number.internal.NumberStateImpl
import com.algolia.instantsearch.core.number.Computation
import com.algolia.instantsearch.core.number.NumberView

/**
 * [NumberView] for compose.
 */
public interface NumberState<T> : NumberView<T> where T : Number {

    /**
     * Text value.
     */
    public val text: String

    /**
     * Computation value.
     */
    public val computation: Computation<T>
}

/**
 * Creates an instance of [NumberState].
 *
 * @param text initial text value
 * @param computation initial computation value
 */
public fun <T : Number> NumberState(text: String = "", computation: Computation<T> = {}): NumberState<T> {
    return NumberStateImpl(text, computation)
}
