@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.item

import androidx.compose.runtime.Stable
import com.algolia.instantsearch.compose.item.internal.StatsStateImpl
import com.algolia.instantsearch.helper.stats.StatsView

/**
 * [StatsView] for compose.
 */
@Stable
public interface StatsState<T> : StatsView<T> {

    /**
     * State holding stats value.
     */
    public val stats: T
}

/**
 * Creates an instance of [StatsState].
 *
 * @param stats initial stats value
 */
public fun <T> StatsState(stats: T): StatsState<T> {
    return StatsStateImpl(stats)
}

/**
 * Creates an instance of [StatsState] as String.
 *
 * @param stats initial stats value
 */
public fun StatsTextState(stats: String = ""): StatsState<String> {
    return StatsStateImpl(stats)
}
