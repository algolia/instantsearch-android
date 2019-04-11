package refinement

import refinement.RefinementPresenter.SortCriterium.CountDesc
import refinement.RefinementPresenter.SortCriterium.IsRefined
import kotlin.properties.Delegates


abstract class RefinementPresenter<T> {

    enum class SortCriterium {
        IsRefined,
        CountAsc,
        CountDesc,
        AlphabeticalAsc,
        AlphabeticalDesc
    }

    private var data: List<Pair<T, Boolean>> = listOf()

    protected abstract val sortComparator: Comparator<RefinedData<T>>

    val dataListeners: MutableList<(List<RefinedData<T>>) -> Unit> = mutableListOf()
    var limit by Delegates.observable(10) { _, _, _ ->
        setData(data)
    }
    val sortCriteria by Delegates.observable(setOf(IsRefined, CountDesc)) { _, _, _ ->
        setData(data)
    }

    var mode by Delegates.observable(SelectionMode.SingleChoice) { _, _, _ ->
        setData(data)
    }

    fun setData(data: List<RefinedData<T>>) {
        this.data = data.sortedWith(sortComparator).take(limit)
    }
}