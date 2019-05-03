package refinement.filter

import filter.FilterGroupID
import filter.Filters
import refinement.RefinementOperator
import refinement.RefinementOperator.Or
import refinement.toGroupID
import search.SearcherSingleIndex
import search.addFacet
import selection.SelectableView


public fun RefinementFilterViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
    operator: RefinementOperator = Or,
    groupName: String = item.attribute.raw
) {
    fun whenSelectedComputedThenUpdateFilterState(groupID: FilterGroupID) {
        onSelectedComputed += { selected ->
            searcher.filterState.notify {
                if (selected) add(groupID, item) else remove(groupID, item)
            }
        }
    }

    fun whenFilterStateChangedThenUpdateSelected(groupID: FilterGroupID) {
        val onChange: (Filters) -> Unit = { filters ->
            isSelected = filters.contains(groupID, item)
        }

        onChange(searcher.filterState)
        searcher.filterState.onChange += onChange
    }

    val groupID = operator.toGroupID(groupName)

    searcher.query.addFacet(item.attribute)
    whenSelectedComputedThenUpdateFilterState(groupID)
    whenFilterStateChangedThenUpdateSelected(groupID)
}

fun RefinementFilterViewModel.connectView(
    view: SelectableView,
    presenter: RefinementFilterPresenter = RefinementFilterPresenter()
) {
    view.setText(presenter(item))
    view.setSelected(isSelected)
    view.onClick = (::setIsSelected)
    onSelectedChanged += { isSelected -> view.setSelected(isSelected) }
}