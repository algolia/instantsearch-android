package com.algolia.instantsearch.compose.searchbox.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.internal.trace
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.Callback

/**
 * [SearchBoxState] implementation.
 */
internal class SearchBoxStateImpl(
    query: String
) : SearchBoxState {

    @set:JvmName("_query")
    override var query by mutableStateOf(query)
    override var onQueryChanged: Callback<String?>? = null
    override var onQuerySubmitted: Callback<String?>? = null

    init {
        trace()
    }

    override fun setText(text: String?, submitQuery: Boolean) {
        this.query = text ?: ""
        if (submitQuery) onQuerySubmitted?.invoke(text) else onQueryChanged?.invoke(text)
    }
}
