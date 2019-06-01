package com.algolia.instantsearch.core.item

import kotlin.properties.Delegates


public open class ItemViewModel<T> {

    public var item: T? by Delegates.observable<T?>(null) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            onItemChanged.forEach { it(newValue) }
        }
    }

    public val onItemChanged: MutableList<(T?) -> Unit> = mutableListOf()
}