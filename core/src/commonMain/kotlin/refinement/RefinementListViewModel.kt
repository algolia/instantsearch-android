package refinement

import helper.RefinementListListener
import helper.RefinementListener
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
            selected = selected.filter { it in newValue }
        }
    }

    var selected by Delegates.observable(listOf<T>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            selectionListeners.forEach { it(newValue) }
        }
    }

    val onSelectedRefinement = { refinement: T? ->
        if (refinement == null) {
            if (mode == Mode.SingleChoice) selected = listOf()
        } else selected = when (mode) {
            Mode.SingleChoice -> listOf(refinement)
            Mode.MultipleChoices -> if (refinement in selected) selected - refinement else selected + refinement
        }
    }
}