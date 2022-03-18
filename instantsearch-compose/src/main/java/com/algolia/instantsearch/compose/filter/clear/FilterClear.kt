package com.algolia.instantsearch.compose.filter.clear

import com.algolia.instantsearch.compose.filter.clear.internal.FilterClearImpl
import com.algolia.instantsearch.filter.clear.FilterClearView

/**
 * [FilterClearView] for compose.
 */
public interface FilterClear : FilterClearView {

    /**
     * Triggers filter clear.
     */
    public fun clear()
}

/**
 * Creates an instance of [FilterClear].
 */
public fun FilterClear(): FilterClear {
    return FilterClearImpl()
}
