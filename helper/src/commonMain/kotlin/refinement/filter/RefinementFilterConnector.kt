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
    fun whenIsSelectedComputedThenUpdateFilterState(groupID: FilterGroupID) {
        onIsSelectedComputed += { isSelected ->
            searcher.filterState.notify {
                if (isSelected) add(groupID, item) else remove(groupID, item)
            }
        }
    }

    fun whenFilterStateChangedThenUpdateIsSelected(groupID: FilterGroupID) {
        val onChange: (Filters) -> Unit = { filters ->
            isSelected = filters.contains(groupID, item)
        }

        onChange(searcher.filterState)
        searcher.filterState.onChange += onChange
    }

    val groupID = operator.toGroupID(groupName)

    searcher.query.addFacet(item.attribute)
    whenIsSelectedComputedThenUpdateFilterState(groupID)
    whenFilterStateChangedThenUpdateIsSelected(groupID)
}

fun RefinementFilterViewModel.connectView(
    view: SelectableView,
    presenter: RefinementFilterPresenter = RefinementFilterPresenter()
) {
    view.setText(presenter(item))
    view.setIsSelected(isSelected)
    view.onClick = (::computeIsSelected)
    onIsSelectedChanged += (view::setIsSelected)
}