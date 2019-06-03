package com.algolia.instantsearch.core.selectable.number

import com.algolia.instantsearch.core.item.ItemView


public interface SelectableNumberView<T: Number>: ItemView<T?> {

    public fun setComputation(computation: Computation<T>)
}