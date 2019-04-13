package refinement

import kotlin.properties.Delegates


public abstract class RefinementListPresenter<T>(limit: Int = 10) {

    protected abstract val comparator: Comparator<SelectedRefinement<T>>

    public var refinements: List<Pair<T, Boolean>> = listOf()
        set(value) {
            field = value.sortedWith(comparator).take(limit)
            listeners.forEach { it(field) }
        }

    public val listeners: MutableList<(List<SelectedRefinement<T>>) -> Unit> = mutableListOf()

    public var limit by Delegates.observable(limit) { _, _, _ ->
        refinements = refinements
    }
}