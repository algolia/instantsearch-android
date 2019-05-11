package selectable.filters

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.Filters
import searcher.SearcherSingleIndex
import searcher.addFacet
import selectable.filter.SelectableFilterPresenter


public fun SelectableFiltersViewModel.connectSearcher(
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

public fun SelectableFiltersViewModel.connectView(
    view: SelectableFiltersView,
    presenter: (Filter) -> String = SelectableFilterPresenter
) {
    view.setItems(items.map { it.key to presenter(it.value) }.toMap())
    view.setSelected(selected)
    view.onClick = (::computeSelected)
    onSelectedChanged += (view::setSelected)
}