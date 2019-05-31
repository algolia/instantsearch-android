package com.algolia.instantsearch.core.item

import kotlin.properties.Delegates


public open class ItemViewModel<T> {

    var item: T? by Delegates.observable<T?>(null) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            onItemChanged.forEach { it(newValue) }
        }
    }

    val onItemChanged: MutableList<(T?) -> Unit> = mutableListOf()
}