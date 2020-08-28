package com.algolia.instantsearch.core.number

/**
 * The view that renders the numeric value.
 */
public interface NumberView<T : Number> {

    public fun setText(text: String)

    public fun setComputation(computation: Computation<T>)
}
