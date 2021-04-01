package com.algolia.instantsearch.compose.filter

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.algolia.instantsearch.compose.filter.internal.FacetListComposeImpl
import com.algolia.instantsearch.core.selectable.list.SelectableItem
import com.algolia.instantsearch.helper.filter.facet.FacetListView
import com.algolia.search.model.search.Facet

public interface FacetListCompose : FacetListView {
    public val facets: State<List<SelectableItem<Facet>>>
}

public fun FacetListCompose(facets: MutableState<List<SelectableItem<Facet>>>): FacetListCompose {
    return FacetListComposeImpl(facets)
}

public fun FacetListCompose(facets: List<SelectableItem<Facet>> = emptyList()): FacetListCompose {
    return FacetListComposeImpl(mutableStateOf(facets))
}
