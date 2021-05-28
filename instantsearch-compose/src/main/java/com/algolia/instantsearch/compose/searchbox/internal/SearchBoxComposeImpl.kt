package com.algolia.instantsearch.compose.searchbox.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.searchbox.SearchQuery
import com.algolia.instantsearch.core.Callback

/**
 * [SearchQuery] implementation.
 */
internal class SearchBoxComposeImpl(
    query: String
) : SearchQuery {

    override var query by mutableStateOf(query)
    override var onQueryChanged: Callback<String?>? = null
    override var onQuerySubmitted: Callback<String?>? = null
}
