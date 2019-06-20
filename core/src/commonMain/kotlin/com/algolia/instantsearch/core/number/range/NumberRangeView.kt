package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.event.EventView
import com.algolia.instantsearch.core.item.ItemView


public interface NumberRangeView<T> : ItemView<Range<T>?>, EventView<Range<T>> where T : Number, T : Comparable<T> {

    fun setBounds(bounds: Range<T>?)
}