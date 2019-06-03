package com.algolia.instantsearch.core.selectable

import com.algolia.instantsearch.core.item.ItemViewModel
import kotlin.properties.Delegates


public open class SelectableItemViewModel<V>(
    item: V
): ItemViewModel<V>(item) {

    public val onIsSelectedChanged: MutableList<(Boolean) -> Unit> = mutableListOf()
    public val onIsSelectedComputed: MutableList<(Boolean) -> Unit> = mutableListOf()

    public var isSelected by Delegates.observable(false) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            onIsSelectedChanged.forEach { it(newValue) }
        }
    }

    public fun computeIsSelected(isSelected: Boolean) {
        onIsSelectedComputed.forEach { it(isSelected) }
    }
}