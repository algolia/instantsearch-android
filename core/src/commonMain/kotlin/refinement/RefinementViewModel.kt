package refinement

import kotlin.properties.Delegates


public class RefinementViewModel<T> {

    public var persistentSelection: Boolean = false

    public val refinementListeners: MutableList<(T?) -> Unit> = mutableListOf()
    public val selectionListeners: MutableList<(T?) -> Unit> = mutableListOf()

    public var refinement: T? by Delegates.observable<T?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            if (!persistentSelection && refinement != selected) {
                selected = null
            }
            refinementListeners.forEach { it(newValue) }
        }
    }
    public var selected: T? by Delegates.observable<T?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            selectionListeners.forEach { it(newValue) }
        }
    }

    public fun select(refinement: T?) {
        selected = if (selected == refinement) null else refinement
    }
}