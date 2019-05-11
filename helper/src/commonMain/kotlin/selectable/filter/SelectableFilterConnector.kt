package selectable.filter

import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.Filters
import search.SearcherSingleIndex
import search.addFacet
import selectable.SelectableView


public fun SelectableFilterViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
    groupID: FilterGroupID = FilterGroupID.Or(item.attribute)
) {
    fun whenIsSelectedComputedThenUpdateFilterState() {
        onIsSelectedComputed += { isSelected ->
            searcher.filterState.notify {
                if (isSelected) add(groupID, item) else remove(groupID, item)
            }
        }
    }

    fun whenFilterStateChangedThenUpdateIsSelected() {
        val onChange: (Filters) -> Unit = { filters ->
            isSelected = filters.contains(groupID, item)
        }

        onChange(searcher.filterState)
        searcher.filterState.onChange += onChange
    }

    searcher.query.addFacet(item.attribute)
    whenIsSelectedComputedThenUpdateFilterState()
    whenFilterStateChangedThenUpdateIsSelected()
}

fun SelectableFilterViewModel.connectView(
    view: SelectableView,
    presenter: (Filter) -> String = SelectableFilterPresenter
) {
    view.setText(presenter(item))
    view.setIsSelected(isSelected)
    view.onClick = (::computeIsSelected)
    onIsSelectedChanged += (view::setIsSelected)
}