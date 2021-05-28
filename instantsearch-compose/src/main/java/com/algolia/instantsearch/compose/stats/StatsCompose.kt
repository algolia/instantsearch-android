package com.algolia.instantsearch.compose.stats

import androidx.compose.runtime.Stable
import com.algolia.instantsearch.compose.stats.internal.StatsComposeImpl
import com.algolia.instantsearch.helper.stats.StatsView

/**
 * [StatsView] for compose.
 */
@Stable
public interface StatsCompose<T> : StatsView<T> {

    /**
     * State holding stats value.
     */
    public var stats: T

    override fun setText(text: T) {
        stats = text
    }
}

/**
 * Creates an instance of [StatsCompose].
 *
 * @param stats initial stats value
 */
public fun <T> StatsCompose(stats: T): StatsCompose<T> {
    return StatsComposeImpl(stats)
}

/**
 * Creates an instance of [StatsCompose] as String.
 *
 * @param stats initial stats value
 */
public fun StatsTextCompose(stats: String = ""): StatsCompose<String> {
    return StatsComposeImpl(stats)
}
