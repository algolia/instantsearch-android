package selectable

import kotlin.properties.Delegates


public open class SelectableViewModel<V>(
    val item: V,
    isSelected: Boolean = false
) {

    val onIsSelectedChanged: MutableList<(Boolean) -> Unit> = mutableListOf()
    val onIsSelectedComputed: MutableList<(Boolean) -> Unit> = mutableListOf()

    public var isSelected by Delegates.observable(isSelected) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onIsSelectedChanged.forEach { it(newValue) }
        }
    }

    public fun computeIsSelected(isSelected: Boolean) {
        onIsSelectedComputed.forEach { it(isSelected) }
    }
}