package refinement.facet

import com.algolia.search.model.Attribute
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Facet
import filter.FilterGroupID
import filter.Filters
import logging.debugLog
import refinement.RefinementOperator
import refinement.RefinementOperator.And
import refinement.RefinementOperator.Or
import search.SearcherSingleIndex

//TODO: Remove logs
//FIXME: Synchronize a RefinementFacet widget(color, red, OR) with a FacetState having a Refinement(color, red, AND)
// -> Maybe we should expose a `getFacets(groupName)` that returns all facets for that groupName,
// used for syncing related views?
public fun RefinementFacetViewModel.connectSearcher(
    attribute: Attribute,
    searcher: SearcherSingleIndex,
    refinement: Facet,
    operator: RefinementOperator = Or,
    groupName: String = attribute.raw
) {
    item = refinement // We need to set item now, otherwise as selected is boolean we won't know what gets selected
    val groupID = when (operator) {
        And -> FilterGroupID.And(groupName)
        Or -> FilterGroupID.Or(groupName)
    }
    val onFilterStateChange: (Filters) -> Unit = { filters ->
        selected = filters.getFacets(groupID).orEmpty()
            .any { it.value.raw?.equals(item?.value) ?: false }
    }

    onFilterStateChange(searcher.filterState)
    searcher.filterState.onChange += onFilterStateChange
    searcher.onResponseChanged += { response ->
        val facets = response.disjunctiveFacetsOrNull?.get(attribute) ?: response.facetsOrNull?.get(attribute)
        facets?.find { it.value == item?.value }?.let { item = it }
    }
    onSelectionComputed += { selection ->
        searcher.filterState.notify {
            clear(groupID)
            item?.let {
                val filter = Filter.Facet(attribute, item!!.value)
                if (selection) add(groupID, filter) else remove(groupID, filter)
                debugLog("Computed new filter: $filter, ${if (selection) "add" else "remov"}ed")
            }
            debugLog("Computed new filter: null")
        }
    }
}

fun RefinementFacetViewModel.connectView(
    view: RefinementFacetView
) {
    fun assignSelectableItem(facet: Facet?, selected: Boolean) {
        debugLog("Assigning item: $facet, selected=$selected")
        view.setSelectableItem(facet?.let { RefinementFacet(facet, selected) })
    }

    assignSelectableItem(item, selected)
    view.onClick = { facet ->
        debugLog("view.onClick -> let's select/deselect the clicked facet $facet!")
        toggleSelection()
    }
    onItemChanged += { item ->
        debugLog("VM.onItemChanged -> let's assign the new item $item with current selected $selected!")
        assignSelectableItem(item, selected)
    }
    onSelectionChanged += { selection ->
        debugLog("VM.onSelectionChanged -> let's assign the new selected $selection with current item $item!")
        assignSelectableItem(item, selection)
    }
}