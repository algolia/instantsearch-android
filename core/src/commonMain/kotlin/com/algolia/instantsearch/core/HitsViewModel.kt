package com.algolia.instantsearch.core

import kotlin.properties.Delegates

//TODO: Refactor as ListViewModel, superclass of SelectableListViewModel?
class ListViewModel<T>(items: List<T> = listOf()) {
    public val onItemsChanged: MutableList<(List<T>) -> Unit> = mutableListOf()

    public var items: List<T> by Delegates.observable(items) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            onItemsChanged.forEach { it(newValue) }
        }
    }
}