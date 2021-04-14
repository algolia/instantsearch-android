package com.algolia.instantsearch.compose.stats.internal

import androidx.compose.runtime.MutableState
import com.algolia.instantsearch.compose.stats.StatsCompose

internal class StatsComposeImpl<T>(
    override val state: MutableState<T>
) : StatsCompose<T> {

    override fun setText(text: T) {
        state.value = text
    }
}
