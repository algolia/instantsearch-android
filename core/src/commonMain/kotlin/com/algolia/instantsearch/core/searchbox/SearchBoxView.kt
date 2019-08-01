package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.Callback


public interface SearchBoxView {

    public var onQueryChanged: Callback<String?>?
    public var onQuerySubmitted: Callback<String?>?

    public fun setText(text: String?)
}