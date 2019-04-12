package refinement

import refinement.SelectionMode.MultipleChoice
import refinement.SelectionMode.SingleChoice
import kotlin.properties.Delegates


public class RefinementListViewModel<T>(
    public val mode: SelectionMode = SingleChoice
) {

    /**
     * When `true`, the [selected] refinements are kept even when they are not present in [refinements] anymore.
     * Note that if [selected] refinements are present when new [refinements] are set, they always will be kept.
     */
    public var persistentSelection: Boolean = false

    public val refinementListeners: MutableList<(List<T>) -> Unit> = mutableListOf()
    public val selectionListeners: MutableList<(List<T>) -> Unit> = mutableListOf()

    public var refinements: List<T> by Delegates.observable(listOf()) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            refinementListeners.forEach { it(newValue) }
            if (!persistentSelection) {
                selected = selected.filter { it in newValue }
            }
        }
    }
    public var selected by Delegates.observable(listOf<T>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            selectionListeners.forEach { it(newValue) }
        }
    }

    public fun select(refinement: T) {
        selected = when (mode) {
            SingleChoice -> if (refinement in selected) listOf() else listOf(refinement)
            MultipleChoice -> if (refinement in selected) selected - refinement else selected + refinement
        }
    }
}