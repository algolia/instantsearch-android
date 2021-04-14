package com.algolia.instantsearch.compose.searchbox

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.algolia.instantsearch.compose.searchbox.internal.SearchBoxComposeImpl
import com.algolia.instantsearch.core.searchbox.SearchBoxView

public interface SearchBoxCompose : SearchBoxView {
    public val query: State<String>
    public fun onValueChange(query: String, isSubmit: Boolean)
}

public fun SearchBoxCompose(query: MutableState<String>): SearchBoxCompose {
    return SearchBoxComposeImpl(query)
}

public fun SearchBoxCompose(query: String = ""): SearchBoxCompose {
    return SearchBoxComposeImpl(mutableStateOf(query))
}
