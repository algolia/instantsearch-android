package com.algolia.instantsearch.core.selectable.number


public interface SelectableNumberView<T: Number> {

    fun setComputation(computation: Computation<T>)

    fun setNumber(number: T?)
}