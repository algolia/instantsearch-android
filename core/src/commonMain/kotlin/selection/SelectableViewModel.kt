package selection

import kotlin.properties.Delegates

public open class SelectableViewModel<V> {
    val onItemChanged: MutableList<(V?) -> Unit> = mutableListOf()
    var onSelectionChanged: List<(Boolean) -> Unit> = mutableListOf()
    var onSelectionComputed: List<(Boolean) -> Unit> = mutableListOf()

    public var item by Delegates.observable<V?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) onItemChanged.forEach { it(newValue) }
    }
    //TODO: Rename as selected
    public var selection by Delegates.observable(false) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onSelectionChanged.forEach { it(newValue) }
        }
    }

    public fun toggleSelection() {
        selection = !selection
        onSelectionComputed.forEach { it(selection) }
    }
}
