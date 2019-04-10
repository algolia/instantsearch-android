package refinement

import kotlin.properties.Delegates


class RefinementViewModel<T> {

    val refinementListeners: MutableList<(T?) -> Unit> = mutableListOf()
    val selectionListeners: MutableList<(T?) -> Unit> = mutableListOf()
    var persistentSelection: Boolean = false
    var refinement: T? by Delegates.observable<T?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            if (!persistentSelection && refinement != selected) {
                selected = null
            }
            refinementListeners.forEach { it(newValue) }
        }
    }
    var selected: T? by Delegates.observable<T?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            selectionListeners.forEach { it(newValue) }
        }
    }

    fun select(refinement: T?) {
        selected = if (selected == refinement) null else refinement
    }
}