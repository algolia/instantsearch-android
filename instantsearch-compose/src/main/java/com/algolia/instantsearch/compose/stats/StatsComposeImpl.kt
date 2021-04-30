package com.algolia.instantsearch.compose.stats

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.algolia.instantsearch.compose.stats.internal.StatsComposeImpl
import com.algolia.instantsearch.helper.stats.StatsView

/**
 * Stats view for compose.
 */
public interface StatsCompose<T> : StatsView<T> {

    /**
     * State holding stats value.
     */
    public val stats: State<T>
}

/**
 * Creates an instance of [StatsCompose].
 *
 * @param state mutable state holding stats
 */
public fun <T> StatsCompose(state: MutableState<T>): StatsCompose<T> {
    return StatsComposeImpl(state)
}

/**
 * Creates an instance of [StatsCompose] as String.
 *
 * @param state mutable string state holding stats
 */
public fun StatsTextCompose(state: MutableState<String> = mutableStateOf("")): StatsCompose<String> {
    return StatsComposeImpl(state)
}
