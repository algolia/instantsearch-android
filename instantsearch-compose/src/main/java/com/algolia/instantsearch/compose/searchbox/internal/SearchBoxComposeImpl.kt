package com.algolia.instantsearch.compose.searchbox.internal

import androidx.compose.runtime.MutableState
import com.algolia.instantsearch.compose.searchbox.SearchBoxCompose
import com.algolia.instantsearch.core.Callback

/**
 * [SearchBoxCompose] implementation.
 */
internal class SearchBoxComposeImpl(
    override val query: MutableState<String>
) : SearchBoxCompose {

    override var onQueryChanged: Callback<String?>? = null
    override var onQuerySubmitted: Callback<String?>? = null

    override fun setText(text: String?, submitQuery: Boolean) {
        query.value = text ?: ""
    }

    override fun onValueChange(query: String, isSubmit: Boolean) {
        val onQuery = if (isSubmit) onQuerySubmitted else onQueryChanged
        onQuery?.invoke(query)
    }
}
