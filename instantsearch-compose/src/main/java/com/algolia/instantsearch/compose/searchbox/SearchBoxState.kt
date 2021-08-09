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
    public var query: String

    /**
     * the callback to be triggered on text update
     *
     * @param query received query input
     * @param isSubmit true if the value is a submit, otherwise false
     */
    public fun changeValue(query: String, isSubmit: Boolean)
}

/**
 * Creates an instance of [SearchBoxState].
 *
 * @param query default query value
 */
public fun SearchBoxState(query: String = ""): SearchBoxState {
    return SearchBoxStateImpl(query)
}

@Deprecated("use SearchBoxState instead", replaceWith = ReplaceWith("SearchBoxState"))
public typealias SearchQuery = SearchBoxState

@Deprecated("use SearchBoxState instead", replaceWith = ReplaceWith("SearchBoxState(query)"))
public fun SearchQuery(query: String = ""): SearchQuery = SearchBoxState(query)
