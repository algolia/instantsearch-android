package selectable.numeric

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.Filters
import filter.add
import searcher.SearcherSingleIndex


public fun SelectableNumericsViewModel.connectSearcher(
    attribute: Attribute,
    searcher: SearcherSingleIndex,
    groupID: FilterGroupID = FilterGroupID.And(attribute)
) {
    fun onSelectionsComputedThenUpdateFilterState() {
        onSelectionsComputed += { selections ->
            searcher.filterState.notify {
                clear(groupID)
                add(groupID, selections)
            }
        }
    }

    fun onFilterStateChangedThenUpdateSelections() {
        val onChange: (Filters) -> Unit = { filters ->
            selections = filters.getNumericFilters(groupID).orEmpty()
        }

        onChange(searcher.filterState)
        searcher.filterState.onChange += onChange
    }

    onSelectionsComputedThenUpdateFilterState()
    onFilterStateChangedThenUpdateSelections()
}

public fun SelectableNumericsViewModel.connectView(
    view: SelectableNumericsView
) {

    fun setSelectableItems(filters: List<Filter.Numeric>, selections: Set<Filter.Numeric>) {
        val selectableItems = filters.map { SelectableNumeric(it, selections.contains(it)) }

        view.setSelectableItems(selectableItems)
    }

    setSelectableItems(items, selections)
    view.onClick = { filter -> computeSelections(filter) }
    onItemsChanged += { items -> setSelectableItems(items, selections) }
    onSelectionsChanged += { selections -> setSelectableItems(items, selections) }
}