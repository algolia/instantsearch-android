@file:Suppress("FunctionName")

package com.algolia.instantsearch.compose.hits

import com.algolia.instantsearch.compose.hits.internal.HitsStateImpl
import com.algolia.instantsearch.core.hits.HitsView

/**
 * [HitsView] for compose.
 */
public interface HitsState<T> : HitsView<T> {

    /**
     * Hits list.
     */
    public val hits: List<T>
}

/**
 * Creates an instance of [HitsState].
 *
 * @param hits initial hits list value
 */
public fun <T> HitsState(hits: List<T>): HitsState<T> {
    return HitsStateImpl(hits)
}
