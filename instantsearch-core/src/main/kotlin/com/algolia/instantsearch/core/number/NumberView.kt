package com.algolia.instantsearch.core.number


public interface NumberView<T : Number> {

    public fun setText(text: String)

    public fun setComputation(computation: Computation<T>)
}