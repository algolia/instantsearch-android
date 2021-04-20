package com.algolia.instantsearch.compose.searchbox

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.algolia.instantsearch.compose.searchbox.internal.SearchBoxComposeImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxView

/**
 * Search Box view for compose.
 */
public interface SearchBoxCompose : SearchBoxView {

    /**
     * Search box query.
     */
    public val query: MutableState<String>

    /**
     * the callback to be triggered on text update
     */
    public fun onValueChange(query: String, isSubmit: Boolean)
}

/**
 * Creates an instance of [SearchBoxCompose].
 *
 * @param query mutable state holding the query
 */
public fun SearchBoxCompose(query: MutableState<String>): SearchBoxCompose {
    return SearchBoxComposeImpl(query)
}

/**
 * Creates an instance of [SearchBoxCompose].
 *
 * @param query default query value
 */
public fun SearchBoxCompose(query: String = ""): SearchBoxCompose {
    return SearchBoxComposeImpl(mutableStateOf(query))
}
