package refinement

import refinement.RefinementPresenter.SortCriteria.CountDesc
import refinement.RefinementPresenter.SortCriteria.IsRefined


abstract class RefinementPresenter<T> {

    enum class SortCriteria {
        IsRefined,
        CountAsc,
        CountDesc,
        AlphabeticalAsc,
        AlphabeticalDesc
    }

    val limit: Int = 10
    val persistentSelection: Boolean = false
    val sortOrder: List<SortCriteria> = listOf(IsRefined, CountDesc)
    val selectionMode: SelectionMode = SelectionMode.SingleChoice

    var data: List<Pair<T, Boolean>> = listOf()
        set(values) {
            field = sortData(values)
        }
    val dataListeners: MutableList<(List<RefinedData<T>>) -> Unit> = mutableListOf()

    abstract val sortComparator: Comparator<Pair<T, Boolean>>

    abstract fun sortData(data: List<Pair<T, Boolean>>): List<Pair<T, Boolean>>
}