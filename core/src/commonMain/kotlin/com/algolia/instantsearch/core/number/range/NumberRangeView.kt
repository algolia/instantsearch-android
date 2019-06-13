package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.item.ItemView
import com.algolia.instantsearch.core.number.Range


public interface NumberRangeView<T : Number> : ItemView<Range<T>> {

    // UI: Price from [10] to [100]
    // UI: Price Slider 0...MIN...MAX...100
    public var onNewRange: ((Range<T>) -> Unit)?
}