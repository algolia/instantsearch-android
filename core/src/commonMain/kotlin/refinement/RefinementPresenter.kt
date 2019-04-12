package refinement

import kotlin.properties.Delegates


public abstract class RefinementPresenter<T> {

    protected abstract val comparator: Comparator<RefinedData<T>>

    public var data: List<Pair<T, Boolean>> = listOf()
        set(value) {
            field = value.sortedWith(comparator).take(limit)
        }

    public val dataListeners: MutableList<(List<RefinedData<T>>) -> Unit> = mutableListOf()

    public var limit by Delegates.observable(10) { _, _, _ ->
        data = data
    }
}