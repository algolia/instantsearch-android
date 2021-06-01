package com.algolia.instantsearch.compose.stats.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.stats.StatsState

/**
 * [StatsState] implementation.
 */
internal class StatsStateImpl<T>(stats: T) : StatsState<T> {

    override var stats by mutableStateOf(stats)

    override fun setText(text: T) {
        this.stats = text
    }
}
