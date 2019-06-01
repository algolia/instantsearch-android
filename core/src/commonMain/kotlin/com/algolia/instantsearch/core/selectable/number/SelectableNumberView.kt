package com.algolia.instantsearch.core.selectable.number


public interface SelectableNumberView<T: Number> {

    public fun setComputation(computation: Computation<T>)

    public fun setNumber(number: T?)
}