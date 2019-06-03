package com.algolia.instantsearch.core.searchbox

import com.algolia.instantsearch.core.item.ItemViewModel


public open class SearchBoxViewModel : ItemViewModel<String?>(null) {

    public fun submitQuery() {
        onQuerySubmitted.forEach { it(item) }
    }

    public val onQuerySubmitted: MutableList<(String?) -> Unit> = mutableListOf()
}