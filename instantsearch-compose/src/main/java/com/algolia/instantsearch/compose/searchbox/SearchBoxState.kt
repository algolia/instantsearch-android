package com.algolia.instantsearch.compose.searchbox

import com.algolia.instantsearch.compose.searchbox.internal.SearchBoxStateImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxView

/**
 * Search box query component for compose.
 */
public interface SearchBoxState : SearchBoxView {

    /**
     * Search box query.
     */
    public val query: String
}

/**
 * Creates an instance of [SearchBoxState].
 *
 * @param query default query value
 */
public fun SearchBoxState(query: String = ""): SearchBoxState {
    return SearchBoxStateImpl(query)
}
