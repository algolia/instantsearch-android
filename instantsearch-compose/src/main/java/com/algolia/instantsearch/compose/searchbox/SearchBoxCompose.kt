package com.algolia.instantsearch.compose.searchbox

import androidx.compose.runtime.Stable
import com.algolia.instantsearch.compose.searchbox.internal.SearchBoxComposeImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxView

/**
 * Search Box view for compose.
 */
@Stable
public interface SearchBoxCompose : SearchBoxView {

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
    public fun onValueChange(query: String, isSubmit: Boolean) {
        val onQuery = if (isSubmit) onQuerySubmitted else onQueryChanged
        onQuery?.invoke(query)
    }

    override fun setText(text: String?, submitQuery: Boolean) {
        query = text ?: ""
        if (submitQuery) onQuerySubmitted?.invoke(text)
    }
}

/**
 * Creates an instance of [SearchBoxCompose].
 *
 * @param query default query value
 */
public fun SearchBoxCompose(query: String = ""): SearchBoxCompose {
    return SearchBoxComposeImpl(query)
}
