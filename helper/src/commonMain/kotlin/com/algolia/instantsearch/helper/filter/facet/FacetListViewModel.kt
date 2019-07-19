package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.search.model.search.Facet


public class FacetListViewModel(
    items: List<Facet> = listOf(),
    selectionMode: SelectionMode = SelectionMode.Multiple,
    public val persistentSelection: Boolean = false
) : SelectableListViewModel<String, Facet>(items, selectionMode) {

    fun getFacetListItems(): List<FacetListItem> {
        val facets = items.value.map { FacetListItem(it, selections.value.contains(it.value)) }

        return if (persistentSelection) {
            selections.value
                .filter { selection -> facets.all { it.first.value != selection } }
                .map { FacetListItem(Facet(it, 0), true) }
                .plus(facets)
        } else facets
    }
}