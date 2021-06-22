package com.algolia.instantsearch.compose.number.relevantsort

import com.algolia.instantsearch.compose.number.relevantsort.internal.RelevantSortStateImpl
import com.algolia.instantsearch.core.relevantsort.RelevantSortView

/**
 * [RelevantSortView] for compose.
 */
public interface RelevantSortState<T> : RelevantSortView<T> {

    /**
     * Current value of relevant sort.
     */
    public val value: T
}

/**
 * Creates an instance of [RelevantSortState].
 *
 * @param value initial value
 */
public fun <T> RelevantSortState(value: T): RelevantSortState<T> {
    return RelevantSortStateImpl(value)
}
