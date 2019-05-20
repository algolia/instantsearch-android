package com.algolia.instantsearch.core.searchbox

import kotlin.properties.Delegates

class SearchBoxViewModel {
    var query by Delegates.observable<String?>(null) { _, old, new ->
        if (old != new) {
            changeListeners.forEach { it(new) }
        }
    }

    fun submit() {
        submitListeners.forEach { it(query) }
    }

    var changeListeners: List<(String?) -> Unit> = listOf()
    var submitListeners: List<(String?) -> Unit> = listOf()
}