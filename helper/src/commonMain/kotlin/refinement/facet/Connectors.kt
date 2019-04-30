package refinement.facet

import com.algolia.search.model.filter.Filter
import filter.FilterGroupID
import filter.Filters
import refinement.RefinementOperator
import refinement.RefinementOperator.And
import refinement.RefinementOperator.Or
import search.SearcherSingleIndex

//FIXME: Synchronize a RefinementFacet widget(color, red, OR) with a FacetState having a Refinement(color, red, AND)
// -> Maybe we should expose a `getFacets(groupName)` that returns all facets for that groupName,
// used for syncing related views?
public fun RefinementFilterViewModel.connectSearcher(
    searcher: SearcherSingleIndex,
    operator: RefinementOperator = Or,
    groupName: String = filter.attribute.raw
) {
    val groupID = when (operator) {
        And -> FilterGroupID.And(groupName)
        Or -> FilterGroupID.Or(groupName)
    }
    val onFilterStateChange: (Filters) -> Unit = { filters ->
        selected = when (filter) {
            is Filter.Facet -> filters.getFacets(groupID).orEmpty().any { it == filter }
            is Filter.Tag -> filters.getTags(groupID).orEmpty().any { it == filter }
            is Filter.Numeric -> filters.getNumerics(groupID).orEmpty().any { it == filter }
        }
    }

    onFilterStateChange(searcher.filterState)
    searcher.filterState.onChange += onFilterStateChange
    onSelectionComputed += { selection ->
        searcher.filterState.notify {
            clear(groupID)
            if (selection) add(groupID, filter) else remove(groupID, filter)
        }
    }
}

fun RefinementFilterViewModel.connectView(
    view: RefinementFilterView
) {
    fun assignSelectableItem(filter: Filter, selected: Boolean) {
        view.setSelectableItem(RefinementFilter(filter, selected))
    }

    assignSelectableItem(item, selected)
    view.onClick = { toggleSelection() }
    onSelectionChanged += { selection -> assignSelectableItem(item, selection) }
}