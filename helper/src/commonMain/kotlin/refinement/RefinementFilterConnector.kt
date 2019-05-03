package refinement

import filter.FilterGroupID
import filter.Filters
import refinement.RefinementOperator.And
import refinement.RefinementOperator.Or
import search.SearcherSingleIndex
import selection.SelectableView


public fun RefinementFilterViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
    operator: RefinementOperator = Or,
    groupName: String = item.attribute.raw
) {

    fun updateQueryFacets() {
        searcher.query.facets = searcher.query.facets.orEmpty().toMutableSet().also {
            it += item.attribute
        }
    }

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

    val groupID = when (operator) {
        And -> FilterGroupID.And(groupName)
        Or -> FilterGroupID.Or(groupName)
    }

    updateQueryFacets()
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