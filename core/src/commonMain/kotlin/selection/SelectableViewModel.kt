package selection

import kotlin.properties.Delegates


public open class SelectableViewModel<V>(val item: V) {

    val onSelectedChanged: MutableList<(Boolean) -> Unit> = mutableListOf()
    val onSelectedComputed: MutableList<(Boolean) -> Unit> = mutableListOf()

    public var isSelected by Delegates.observable(false) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onSelectedChanged.forEach { it(newValue) }
        }
    }

    public fun setIsSelected(isSelected: Boolean) {
        onSelectedComputed.forEach { it(isSelected) }
    }
}