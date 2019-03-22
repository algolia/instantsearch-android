package refinement

import helper.RefinementListener
import helper.RefinementListListener
import kotlin.properties.Delegates


open class RefinementListViewModel<T>(val mode: Mode = Mode.SingleChoice) {

    enum class Mode {
        SingleChoice,
        MultipleChoices
    }

    val refinementListeners = mutableListOf<RefinementListListener<T>>()
    val selectionListeners = mutableListOf<RefinementListListener<T>>()

    var refinements by Delegates.observable(listOf<T>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            refinementListeners.forEach { it(newValue) }
        }
    }

    var selected by Delegates.observable(listOf<T>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            selectionListeners.forEach { it(newValue) }
        }
    }

    val onSelectedRefinement = { refinement: T ->
        selected = when (mode) {
            Mode.SingleChoice -> listOf(refinement)
            Mode.MultipleChoices -> if (refinement in selected) selected - refinement else selected + refinement
        }
    }
}

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