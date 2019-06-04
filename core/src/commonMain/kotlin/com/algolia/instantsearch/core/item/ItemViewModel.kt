package com.algolia.instantsearch.core.item

import kotlin.properties.Delegates


public open class ItemViewModel<T>(item: T) {

    public var item: T by Delegates.observable(item) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            onItemChanged.forEach { it(newValue) }
        }
    }

    public val onItemChanged: MutableList<(T) -> Unit> = mutableListOf()
}