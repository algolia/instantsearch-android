package refinement

import refinement.SelectionMode.MultipleChoice
import refinement.SelectionMode.SingleChoice
import kotlin.properties.Delegates


public abstract class RefinementListViewModel<K, V>(
    val selectionMode: SelectionMode
) {

    /**
     * When `true`, the [selections] refinements are kept even when they are not present in [refinements] anymore.
     * Note that if [selections] refinements are present when new [refinements] are set, they always will be kept.
     */
    public var persistentSelection: Boolean = false

    public val refinementsListeners: MutableList<(List<V>) -> Unit> = mutableListOf()
    public val selectionsListeners: MutableList<(List<K>) -> Unit> = mutableListOf()
    public val selectedListeners: MutableList<(List<K>) -> Unit> = mutableListOf()

    public var refinements: List<V> by Delegates.observable(listOf()) { _, oldValue, newValue ->
        if (newValue != oldValue) {
            refinementsListeners.forEach { it(newValue) }
            if (!persistentSelection) {
                selections = selections.filter { selection -> newValue.any { it.match(selection) } }
            }
        }
    }
    public var selections by Delegates.observable(listOf<K>()) { _, oldValue, newValue ->
        if (oldValue != newValue) {
            selectionsListeners.forEach { it(newValue) }
        }
    }

    public fun select(key: K) {
        val selections = when (selectionMode) {
            SingleChoice -> if (key in selections) listOf() else listOf(key)
            MultipleChoice -> if (key in selections) selections - key else selections + key
        }

        selectedListeners.forEach { it(selections) }
    }

    public abstract fun V.match(key: K): Boolean
}