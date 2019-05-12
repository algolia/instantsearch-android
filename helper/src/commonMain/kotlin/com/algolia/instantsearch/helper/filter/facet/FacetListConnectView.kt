package com.algolia.instantsearch.helper.filter.facet

import com.algolia.search.model.search.Facet


// TODO Demo persistent selection
public fun FacetListViewModel.connectView(
    view: FacetListView,
    //  persistentSelection: Boolean = true,
    presenter: ((List<FacetListItem>) -> List<FacetListItem>)? = null
) {
    fun List<Facet>.toSelectableFacets(selections: Set<String>): List<FacetListItem> {
        return map { FacetListItem(it, selections.contains(it.value)) }
//        val persistentFacets = if (persistentSelection) {
//            selections
//                .filter { selection -> facets.any { it.first.value ==  selection } }
//                .map { Pair(Facet(it, 0), true) }
//        } else listOf()
//        return persistentFacets + facets
    }

    fun setSelectableItems(facets: List<Facet>, selections: Set<String>) {
        val selectableItems = facets.toSelectableFacets(selections)

        view.setSelectableItems(presenter?.invoke(selectableItems) ?: selectableItems)
    }

    setSelectableItems(items, selections)
    view.onClick = { facet -> computeSelections(facet.value) }
    onItemsChanged += { items -> setSelectableItems(items, selections) }
    onSelectionsChanged += { selections -> setSelectableItems(items, selections) }
}