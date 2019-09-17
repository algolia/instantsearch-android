@file:JvmName("NumberRange")

package com.algolia.instantsearch.core.number.range

import com.algolia.instantsearch.core.connection.Connection
import kotlin.jvm.JvmName

/**
 * Connects this NumberRangeViewModel to a NumberRangeView,
 * updating it when the viewModel's data changes.
 */
public fun <T> NumberRangeViewModel<T>.connectView(
    view: NumberRangeView<T>
): Connection where T : Number, T : Comparable<T> {
    return NumberRangeConnectionView(this, view)
}