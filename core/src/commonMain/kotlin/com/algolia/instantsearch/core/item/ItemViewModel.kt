package com.algolia.instantsearch.core.item

import kotlin.properties.Delegates


public open class ItemViewModel<T>(item: T) {

    public var item: T by Delegates.observable(item) { _, _, newValue ->
        onItemChanged.forEach { it(newValue) }
    }

    public val onItemChanged: MutableList<(T) -> Unit> = mutableListOf()
}