package com.algolia.instantsearch.filter.facet

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.core.subscription.mergeSubscription
import com.algolia.instantsearch.extension.traceFacetList

public class FacetListViewModel(
    items: List<FacetHits> = emptyList(),
    selectionMode: SelectionMode = SelectionMode.Multiple,
    public val persistentSelection: Boolean = false,
) : SelectableListViewModel<String, FacetHits>(items, selectionMode) {

    init {
        traceFacetList(items, selectionMode, persistentSelection)
    }

    public val facets: SubscriptionValue<List<Pair<FacetHits, Boolean>>> = mergeSubscription(
        listOf(),
        this.items,
        selections
    ) { items, selections ->
        val facets = items.map { FacetListItem(it, selections.contains(it.value)) }

        if (persistentSelection) {
            selections
                .filter { selection -> facets.all { it.first.value != selection } }
                .map { FacetListItem(FacetHits(it, "", 0), true) }
                .plus(facets)
        } else facets
    }
}
