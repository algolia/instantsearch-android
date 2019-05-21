package com.algolia.instantsearch.core.searchbox

import kotlin.properties.Delegates

/**
 * A ViewModel for SearchBoxView implementations, holding their [query].
 *
 * It lets you define an arbitrary number of listeners,
 * for both [text change][changeListeners] and [submit][submitListeners] events.
 */
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