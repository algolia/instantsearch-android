package refinement.filters

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.Filters
import refinement.filter.RefinementFilterPresenter
import search.SearcherSingleIndex
import search.addFacet


public fun RefinementFiltersViewModel.connectSearcher(
    attribute: Attribute,
    searcher: SearcherSingleIndex,
    groupID: FilterGroupID = FilterGroupID.Or(attribute)
) {
    fun whenSelectedComputedThenUpdateFilterState() {
        onSelectedComputed += { computed ->
            searcher.filterState.notify {
                items[selected]?.let { remove(groupID, it) }
                items[computed]?.let { add(groupID, it) }
            }
        }
    }

    fun whenFilterStateChangedThenUpdateSelected() {
        val onChange: (Filters) -> Unit = { filters ->
            selected = items.entries.find { it.value == filters.getFilters(groupID).firstOrNull() }?.key
        }

        onChange(searcher.filterState)
        searcher.filterState.onChange += onChange
    }

    searcher.query.addFacet(attribute)
    whenSelectedComputedThenUpdateFilterState()
    whenFilterStateChangedThenUpdateSelected()
}

public fun RefinementFiltersViewModel.connectView(
    view: RefinementFiltersView,
    presenter: (Filter) -> String = RefinementFilterPresenter
) {
    view.setItems(items.map { it.key to presenter(it.value) }.toMap())
    view.setSelected(selected)
    view.onClick = (::computeSelected)
    onSelectedChanged += (view::setSelected)
}