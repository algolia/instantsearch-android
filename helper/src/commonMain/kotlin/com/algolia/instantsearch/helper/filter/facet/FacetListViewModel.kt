package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.search.model.search.Facet


class FacetListViewModel(
    items: List<Facet> = listOf(),
    selectionMode: SelectionMode = SelectionMode.Multiple
) : SelectableListViewModel<String, Facet>(items, selectionMode)