package com.algolia.instantsearch.compose.searchbox

import com.algolia.instantsearch.compose.searchbox.internal.SearchBoxComposeImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxView

/**
 * Search box query component for compose.
 */
public interface SearchQuery : SearchBoxView {

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
    public fun onValueChange(query: String, isSubmit: Boolean)
}

/**
 * Creates an instance of [SearchQuery].
 *
 * @param query default query value
 */
public fun SearchQuery(query: String = ""): SearchQuery {
    return SearchBoxComposeImpl(query)
}
