package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.observable.ObservableEvent
import com.algolia.instantsearch.core.observable.ObservableItem


//DISCUSS: minRange?
public open class NumberRangeViewModel<T>(
    range: Range<T>? = null,
    bounds: Range<T>? = null
) where T : Number, T : Comparable<T> {

    public val range = ObservableItem(range)
    public val bounds = ObservableItem(bounds).apply {
        subscribe { coerce(this@NumberRangeViewModel.range.get()) }
    }
    public val event = ObservableEvent<Range<T>?>()

    public fun coerce(range: Range<T>?) {
        val coerced = range?.coerce(bounds.get())

        if (coerced != this.range.get()) event.send(coerced)
    }
}