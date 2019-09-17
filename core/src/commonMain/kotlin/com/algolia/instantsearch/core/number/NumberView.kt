package com.algolia.instantsearch.core.number

/**
 * A View that can display a number as text,
 * and might allow the user to increment/decrement it.
 */
public interface NumberView<T : Number> {

    /**
     * Updates the text to display.
     */
    public fun setText(text: String)

    /**
     * Updates the computation used: if this NumberView allows incrementing/decrementing,
     * you must reset interaction listeners (like onClickListeners) to use the latest computation.
     */
    public fun setComputation(computation: Computation<T>)
}