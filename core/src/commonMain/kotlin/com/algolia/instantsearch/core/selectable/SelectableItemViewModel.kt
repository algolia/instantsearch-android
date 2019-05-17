package com.algolia.instantsearch.core.selectable

import kotlin.properties.Delegates


public open class SelectableItemViewModel<V>(
    val item: V
) {

    val onIsSelectedChanged: MutableList<(Boolean) -> Unit> = mutableListOf()
    val onIsSelectedComputed: MutableList<(Boolean) -> Unit> = mutableListOf()

    public var isSelected by Delegates.observable(false) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onIsSelectedChanged.forEach { it(newValue) }
        }
    }

    public fun computeIsSelected(isSelected: Boolean) {
        onIsSelectedComputed.forEach { it(isSelected) }
    }
}