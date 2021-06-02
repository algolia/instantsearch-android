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
    public val hitsList: List<T>
}

/**
 * Creates an instance of [HitsState].
 *
 * @param hitsList initial hits list value
 */
public fun <T> HitsState(hitsList: List<T>): HitsState<T> {
    return HitsStateImpl(hitsList)
}
