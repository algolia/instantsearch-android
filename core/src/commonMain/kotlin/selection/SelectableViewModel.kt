package selection

import kotlin.properties.Delegates


public open class SelectableViewModel<V>(public val item: V) {

    val onSelectedChanged: MutableList<(Boolean) -> Unit> = mutableListOf()
    val onSelectedComputed: MutableList<(Boolean) -> Unit> = mutableListOf()

    public var selected by Delegates.observable(false) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onSelectedChanged.forEach { it(newValue) }
        }
    }

    public fun setSelection(isSelected: Boolean) {
        selected = isSelected
        onSelectedComputed.forEach { it(isSelected) }
    }
}
