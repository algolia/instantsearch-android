package com.algolia.instantsearch.compose.searchbox

import com.algolia.instantsearch.compose.searchbox.internal.SearchBoxComposeImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxView

/**
 * Search Box view for compose.
 */
public interface SearchBoxCompose : SearchBoxView {

    /**
     * Search box query.
     */
    public var query: String

    /**
     * the callback to be triggered on text update
     */
    public fun onValueChange(query: String, isSubmit: Boolean)
}

/**
 * Creates an instance of [SearchBoxCompose].
 *
 * @param query default query value
 */
public fun SearchBoxCompose(query: String = ""): SearchBoxCompose {
    return SearchBoxComposeImpl(query)
}
