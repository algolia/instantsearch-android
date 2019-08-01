package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.Callback


public interface NumberRangeView<T> where T : Number, T : Comparable<T> {

    public var onRangeChanged: Callback<Range<T>>?

    public fun setRange(range: Range<T>?)

    public fun setBounds(bounds: Range<T>?)
}