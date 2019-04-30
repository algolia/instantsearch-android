package selection

import kotlin.properties.Delegates

public open class SelectableViewModel<V>(public val item: V) {
    val onSelectionChanged: MutableList<(Boolean) -> Unit> = mutableListOf()
    val onSelectionComputed: MutableList<(Boolean) -> Unit> = mutableListOf()

    public var selected by Delegates.observable(false) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onSelectionChanged.forEach { it(newValue) }
        }
    }

    public fun toggleSelection() {
        selected = !selected
        onSelectionComputed.forEach { it(selected) }
    }
}
