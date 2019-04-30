package selection

import kotlin.properties.Delegates

public open class SelectableViewModel<V> {
    val onItemChanged: MutableList<(V?) -> Unit> = mutableListOf()
    val onSelectionChanged: MutableList<(Boolean) -> Unit> = mutableListOf()
    val onSelectionComputed: MutableList<(Boolean) -> Unit> = mutableListOf()

    public var item by Delegates.observable<V?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) onItemChanged.forEach { it(newValue) }
    }
    //TODO: Rename as selected
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
