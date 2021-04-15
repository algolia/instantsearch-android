package com.algolia.instantsearch.compose.stats

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.algolia.instantsearch.compose.stats.internal.StatsComposeImpl
import com.algolia.instantsearch.helper.stats.StatsView

public interface StatsCompose<T> : StatsView<T> {
    public val stats: State<T>
}

public fun <T> StatsCompose(state: MutableState<T>): StatsCompose<T> {
    return StatsComposeImpl(state)
}

public fun StatsTextCompose(state: MutableState<String> = mutableStateOf("")): StatsCompose<String> {
    return StatsComposeImpl(state)
}
