package selection.map

import kotlin.properties.Delegates


public open class SelectableMapViewModel<K, V>(
    val items: Map<K, V>,
    selected: K? = null
) {

    public val onSelectedChanged: MutableList<(K?) -> Unit> = mutableListOf()
    public val onSelectedComputed: MutableList<(K?) -> Unit> = mutableListOf()

    public var selected by Delegates.observable(selected) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            onSelectedChanged.forEach { it(newValue) }
        }
    }

    public fun computeSelected(selected: K) {
        onSelectedComputed.forEach { it(selected) }
    }
}
