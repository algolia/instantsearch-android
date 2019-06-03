package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.item.ItemView


public interface SearchBoxView: ItemView<String?> {

    public var onQueryChanged: ((String?) -> Unit)?
    public var onQuerySubmitted: ((String?) -> Unit)?
}