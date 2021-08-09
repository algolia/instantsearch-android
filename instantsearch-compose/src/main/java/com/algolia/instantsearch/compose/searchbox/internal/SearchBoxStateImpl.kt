package com.algolia.instantsearch.compose.searchbox.internal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.algolia.instantsearch.compose.searchbox.SearchBoxState
import com.algolia.instantsearch.core.Callback

/**
 * [SearchBoxState] implementation.
 */
internal class SearchBoxStateImpl(
    query: String
) : SearchBoxState {

    override var query by mutableStateOf(query)
    override var onQueryChanged: Callback<String?>? = null
    override var onQuerySubmitted: Callback<String?>? = null

    /**
     * the callback to be triggered on text update
     *
     * @param query received query input
     * @param isSubmit true if the value is a submit, otherwise false
     */
    override fun changeValue(query: String, isSubmit: Boolean) {
        val onQuery = if (isSubmit) onQuerySubmitted else onQueryChanged
        onQuery?.invoke(query)
    }

    override fun setText(text: String?, submitQuery: Boolean) {
        query = text ?: ""
        if (submitQuery) onQuerySubmitted?.invoke(text)
    }
}
