package com.algolia.instantsearch.helper.widget

import com.algolia.search.model.response.ResponseSearch.Hit
import kotlin.properties.Delegates

class HitsViewModel(items: List<Hit> = listOf()) : ViewModel {
    public val onItemsChanged: MutableList<(List<Hit>) -> Unit> = mutableListOf()

    public var items: List<Hit> by Delegates.observable(items) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            onItemsChanged.forEach { it(newValue) }
        }
    }
}