package refinement

import refinement.RefinementListViewModel.Mode.*
import kotlin.properties.Delegates


class RefinementListViewModel<T>(
    val mode: Mode = SingleChoice
) {

    enum class Mode {
        SingleChoice,
        MultipleChoice
    }

    /**
     * When `true`, the [selected] refinements are kept even when they are not present in [refinements] anymore.
     * Note that if [selected] refinements are present when new [refinements] are set, they always will be kept.
     */
    var persistentSelection: Boolean = false
    val refinementListeners: MutableList<(List<T>) -> Unit> = mutableListOf()
    val selectionListeners: MutableList<(List<T>) -> Unit> = mutableListOf()
    var refinements: List<T> by Delegates.observable(listOf()) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            refinementListeners.forEach { it(newValue) }
            if (!persistentSelection) {
                selected = selected.filter { it in newValue }
            }
        }
    }
    var selected by Delegates.observable(listOf<T>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            selectionListeners.forEach { it(newValue) }
        }
    }

    fun select(refinement: T) {
        selected = when (mode) {
            SingleChoice -> if (refinement in selected) listOf() else listOf(refinement)
            MultipleChoice -> if (refinement in selected) selected - refinement else selected + refinement
        }
    }
}
