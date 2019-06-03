package com.algolia.instantsearch.core.number

import com.algolia.instantsearch.core.item.ItemView


public interface NumberView<T: Number>: ItemView<String> {

    public fun setComputation(computation: Computation<T>)
}