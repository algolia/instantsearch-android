package refinement

import helper.RefinementListListener
import helper.RefinementListener
import kotlin.properties.Delegates

typealias SelectionListListener<T> = (List<T>, List<T>) -> Unit

open class RefinementListViewModel<T>(val mode: Mode = Mode.SingleChoice) {

    enum class Mode {
        SingleChoice,
        MultipleChoices
    }

    val dataListeners = mutableListOf<RefinementListListener<T>>()
    val selectionListeners = mutableListOf<SelectionListListener<T>>()

    var data by Delegates.observable(listOf<T>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            dataListeners.forEach { it(newValue) }
            selected = selected.filter { it in newValue }
        }
    }

    var selected by Delegates.observable(listOf<T>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            selectionListeners.forEach { it(oldValue, newValue) }
        }
    }

    val onSelectedRefinement = { refinement: T, isActive: Boolean ->
        selected = if (mode == Mode.SingleChoice) {
            if (isActive) listOf(refinement) else listOf()
        } else {
            if (isActive) selected + refinement else selected - refinement
        }
    }
}


// TODO: Update to leverage SFS
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

    val onSelectedRefinement = { refinement: T? ->
        selected = if (selected != refinement) refinement else null
    }
}