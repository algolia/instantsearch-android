package refinement

import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.Filters
import refinement.RefinementOperator.And
import refinement.RefinementOperator.Or
import search.SearcherSingleIndex


public fun RefinementFilterViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
    operator: RefinementOperator = Or,
    groupName: String = filter.attribute.raw
) {

    fun updateQueryFacets() {
        searcher.query.facets = searcher.query.facets.orEmpty().toMutableSet().also {
            it += filter.attribute
        }
    }

    fun whenSelectedComputedThenUpdateFilterState(groupID: FilterGroupID) {
        onSelectedComputed += { selection ->
            searcher.filterState.notify {
                clear(groupID)
                if (selection) add(groupID, filter) else remove(groupID, filter)
            }
        }
    }

    fun whenFilterStateChangedThenUpdateSelectied(groupID: FilterGroupID) {
        val onChange: (Filters) -> Unit = { filters ->
            selected = filters.contains(groupID, filter)
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
    whenFilterStateChangedThenUpdateSelectied(groupID)
}

fun RefinementFilterViewModel.connectView(
    view: RefinementFilterView
) {
    fun assignSelectableItem(filter: Filter, selected: Boolean) {
        view.setSelectableItem(RefinementFilter(filter, selected))
    }

    assignSelectableItem(item, selected)
    view.onClick = { toggleSelection() }
    onSelectedChanged += { selection -> assignSelectableItem(item, selection) }
}