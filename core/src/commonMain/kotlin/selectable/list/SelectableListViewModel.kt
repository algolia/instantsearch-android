package selectable.list

import selectable.list.SelectionMode.Multiple
import selectable.list.SelectionMode.Single
import kotlin.properties.Delegates


public open class SelectableListViewModel<K, V>(
    items: List<V> = listOf(),
    selections: Set<K> = setOf(),
    val selectionMode: SelectionMode = Multiple
) {

    public val onItemsChanged: MutableList<(List<V>) -> Unit> = mutableListOf()
    public val onSelectionsChanged: MutableList<(Set<K>) -> Unit> = mutableListOf()
    public val onSelectionsComputed: MutableList<(Set<K>) -> Unit> = mutableListOf()

    public var items: List<V> by Delegates.observable(items) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            onItemsChanged.forEach { it(newValue) }
        }
    }
    public var selections by Delegates.observable(selections) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onSelectionsChanged.forEach { it(newValue) }
        }
    }

    public fun computeSelections(key: K) {
        val selections = when (selectionMode) {
            Single -> if (key in selections) setOf() else setOf(key)
            Multiple -> if (key in selections) selections - key else selections + key
        }

        onSelectionsComputed.forEach { it(selections) }
    }
}