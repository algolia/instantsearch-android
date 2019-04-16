package refinement

import refinement.SelectionMode.MultipleChoice
import refinement.SelectionMode.SingleChoice
import kotlin.properties.Delegates


public class RefinementListViewModel<T>(
    val selectionMode: SelectionMode = SingleChoice
) {

    /**
     * When `true`, the [selections] refinements are kept even when they are not present in [refinements] anymore.
     * Note that if [selections] refinements are present when new [refinements] are set, they always will be kept.
     */
    public var persistentSelection: Boolean = false

    public val refinementsListeners: MutableList<(List<T>) -> Unit> = mutableListOf()
    public val selectionsListeners: MutableList<(List<T>) -> Unit> = mutableListOf()
    public val selectedListeners: MutableList<(List<T>) -> Unit> = mutableListOf()

    public var refinements: List<T> by Delegates.observable(listOf()) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            refinementsListeners.forEach { it(newValue) }
            if (!persistentSelection) {
                selections = selections.filter { it in newValue }
            }
        }
    }
    public var selections by Delegates.observable(listOf<T>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            selectionsListeners.forEach { it(newValue) }
        }
    }

    public fun select(refinement: T) {
        val selections = when (selectionMode) {
            SingleChoice -> if (refinement in selections) listOf() else listOf(refinement)
            MultipleChoice -> if (refinement in selections) selections - refinement else selections + refinement
        }

        selectedListeners.forEach { it(selections) }
    }
}