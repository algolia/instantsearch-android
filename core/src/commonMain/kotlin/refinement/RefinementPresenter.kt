package refinement

import kotlin.properties.Delegates


abstract class RefinementPresenter<T> {

    protected abstract val comparator: Comparator<RefinedData<T>>

    var data: List<Pair<T, Boolean>> = listOf()
        set(value) {
            field = value.sortedWith(comparator).take(limit)
        }

    val dataListeners: MutableList<(List<RefinedData<T>>) -> Unit> = mutableListOf()

    var limit by Delegates.observable(10) { _, _, _ ->
        data = data
    }
}