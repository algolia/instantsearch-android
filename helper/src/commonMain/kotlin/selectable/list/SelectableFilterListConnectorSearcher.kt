package selectable.list

import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.Filters
import filter.add
import searcher.SearcherSingleIndex


public fun SelectableFilterListViewModel.Facet.connectSearcher(
    searcher: SearcherSingleIndex,
    groupID: FilterGroupID = FilterGroupID.Or()
) {
    connect(searcher, groupID) { getFacetFilters(groupID) }
}

public fun SelectableFilterListViewModel.Numeric.connectSearcher(
    searcher: SearcherSingleIndex,
    groupID: FilterGroupID = FilterGroupID.And()
) {
    connect(searcher, groupID) { getNumericFilters(groupID) }
}


public fun SelectableFilterListViewModel.Tag.connectSearcher(
    searcher: SearcherSingleIndex,
    groupID: FilterGroupID = FilterGroupID.And()
) {
    connect(searcher, groupID) { getTagFilters(groupID) }
}

public fun SelectableFilterListViewModel.All.connectSearcher(
    searcher: SearcherSingleIndex,
    groupID: FilterGroupID = FilterGroupID.And()
) {
    connect(searcher, groupID) { getFilters(groupID) }
}

internal inline fun <reified T : Filter> SelectableFilterListViewModel<T>.connect(
    searcher: SearcherSingleIndex,
    groupID: FilterGroupID,
    crossinline getSelections: Filters.() -> Set<T>
) {
    onSelectionsComputed += { selections ->
        searcher.filterState.notify {
            clear(groupID)
            add(groupID, selections)
        }
    }
    val onChange: (Filters) -> Unit = { filters ->
        selections = filters.getSelections()
    }

    onChange(searcher.filterState)
    searcher.filterState.onChange += onChange
}