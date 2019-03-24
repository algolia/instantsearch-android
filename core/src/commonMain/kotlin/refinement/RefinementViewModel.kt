package refinement

import helper.RefinementListener
import kotlin.properties.Delegates

open class RefinementViewModel<T> {

    val refinementListeners = mutableListOf<RefinementListener<T>>()
    val selectionListeners = mutableListOf<RefinementListener<T>>()

    var refinement by Delegates.observable<T?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            refinementListeners.forEach { it(newValue) }
        }
    }
    var selected by Delegates.observable<T?>(null) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            selectionListeners.forEach { it(newValue) }
        }
    }

    val onSelectedRefinement = { refinement: T ->
        selected = if (selected != refinement) refinement else null
    }
}