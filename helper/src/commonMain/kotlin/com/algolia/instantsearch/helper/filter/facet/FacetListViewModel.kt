package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.core.subscription.mergeSubscription
import com.algolia.search.model.search.Facet
import kotlin.jvm.JvmField
import kotlin.jvm.JvmOverloads

/**
 * A ViewModel storing a list of [facets], that can be selected according to a [mode][SelectionMode].
 *
 * @param persistentSelection when `true`, a facet will remain selected
 * even when it is removed from [items].
 */
public class FacetListViewModel @JvmOverloads constructor(
    items: List<Facet> = listOf(),
    selectionMode: SelectionMode = SelectionMode.Multiple,
    @JvmField public val persistentSelection: Boolean = false
) : SelectableListViewModel<String, Facet>(items, selectionMode) {

    @JvmField
    public val facets = mergeSubscription(
        listOf(),
        this.items,
        selections
    ) { items, selections ->
        val facets = items.map { FacetListItem(it, selections.contains(it.value)) }

        if (persistentSelection) {
            selections
                .filter { selection -> facets.all { it.first.value != selection } }
                .map { FacetListItem(Facet(it, 0), true) }
                .plus(facets)
        } else facets
    }
}