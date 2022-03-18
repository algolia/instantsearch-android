package com.algolia.instantsearch.compose.filter.current

import com.algolia.instantsearch.compose.filter.current.internal.FilterCurrentStateImpl
import com.algolia.instantsearch.filter.current.FilterAndID
import com.algolia.instantsearch.filter.current.FilterCurrentView

/**
 * [FilterCurrentView] for compose.
 */
public interface FilterCurrentState : FilterCurrentView {

    /**
     * List of current filters.
     */
    public val filters: List<Pair<FilterAndID, String>>

    /**
     * Trigger a filter selection.
     *
     * @param filterAndID selected filter
     */
    public fun selectFilter(filterAndID: FilterAndID)
}

/**
 * Creates an instance of [FilterCurrentState].
 *
 * @param initial the initial value for filtersList.
 */
public fun FilterCurrentState(initial: List<Pair<FilterAndID, String>> = emptyList()): FilterCurrentState {
    return FilterCurrentStateImpl(initial)
}
