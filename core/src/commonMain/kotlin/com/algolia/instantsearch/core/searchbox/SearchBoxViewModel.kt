package com.algolia.instantsearch.core.searchbox

import kotlin.properties.Delegates

/**
 * A ViewModel for SearchBoxView implementations, holding their [query].
 *
 * It lets you define an arbitrary number of listeners,
 * for both [text change][onChanged] and [submit][onSubmitted] events.
 */
public class SearchBoxViewModel {
    public var query by Delegates.observable<String?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onChanged.forEach { it(newValue) }
        }
    }

    public fun submit() {
        onSubmitted.forEach { it(query) }
    }

    public var onChanged: List<(String?) -> Unit> = listOf()
    public var onSubmitted: List<(String?) -> Unit> = listOf()
}