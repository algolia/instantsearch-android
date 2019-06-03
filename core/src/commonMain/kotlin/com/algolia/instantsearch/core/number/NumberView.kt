package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.item.ItemView


public interface NumberView<T: Number>: ItemView<T?> {

    public fun setComputation(computation: Computation<T>)
}