package com.algolia.instantsearch.core.searchbox

import kotlin.properties.Delegates


public class SearchBoxViewModel {

    public var query by Delegates.observable<String?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onQueryChanged.forEach { it(newValue) }
        }
    }

    public fun submitQuery() {
        onQuerySubmitted.forEach { it(query) }
    }

    public val onQueryChanged: MutableList<(String?) -> Unit> = mutableListOf()
    public val onQuerySubmitted: MutableList<(String?) -> Unit> = mutableListOf()
}