package com.algolia.instantsearch.compose.searchbox.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.searchbox.SearchBoxCompose
import com.algolia.instantsearch.core.Callback

/**
 * [SearchBoxCompose] implementation.
 */
internal class SearchBoxComposeImpl(
    query: String
) : SearchBoxCompose {

    override var query by mutableStateOf(query)
    override var onQueryChanged: Callback<String?>? = null
    override var onQuerySubmitted: Callback<String?>? = null
}
