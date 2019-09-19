package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.Callback

/**
 * A View that can display a numeric range within bounds, and might allow the user to change it.
 */
public interface NumberRangeView<T> where T : Number, T : Comparable<T> {

    /**
     * A callback that you must call when the range changes.
     */
    public var onRangeChanged: Callback<Range<T>>?

    /**
     * Updates the range to display.
     */
    public fun setRange(range: Range<T>?)

    /**
     * Updates the bounds to display.
     */
    public fun setBounds(bounds: Range<T>?)
}