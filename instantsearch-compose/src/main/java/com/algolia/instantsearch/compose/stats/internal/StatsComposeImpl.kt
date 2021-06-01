package com.algolia.instantsearch.compose.stats.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.stats.StatsCompose

/**
 * [StatsCompose] implementation.
 */
internal class StatsComposeImpl<T>(stats: T) : StatsCompose<T> {

    override var stats by mutableStateOf(stats)
}
