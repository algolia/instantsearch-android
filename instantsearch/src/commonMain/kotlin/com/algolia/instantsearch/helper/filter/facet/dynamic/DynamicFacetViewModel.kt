package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import kotlin.properties.Delegates

/**
 * Dynamic facets business logic.
 * Provides an ordered list of attributed facets, facet selections and events.
 */
public class DynamicFacetViewModel(
    orderedFacets: List<AttributedFacets> = emptyList(),
    selections: SelectionsPerAttribute = mutableMapOf(),
    selectionModeForAttribute: Map<Attribute, SelectionMode> = emptyMap()
) {

    /**
     * Ordered list of attributed facets.
     */
    public var orderedFacets: List<AttributedFacets> by Delegates.observable(orderedFacets) { _, _, newValue ->
        onFacetOrderChanged.send(newValue)
        updateFacetLists()
    }

    /**
     * Mapping between a facet attribute and a set of selected facet values.
     */
    public var selections: SelectionsPerAttribute by Delegates.observable(selections) { _, _, newValue ->
        onSelectionsChanged.send(newValue)
    }

    /**
     * Event triggered when the facet order changed externally.
     */
    public val onFacetOrderChanged: SubscriptionEvent<List<AttributedFacets>> = SubscriptionEvent()

    /**
     * Event triggered when the facets values selection changed externally.
     */
    public val onSelectionsChanged: SubscriptionEvent<SelectionsPerAttribute> = SubscriptionEvent()

    /**
     * Event triggered when the facets values selection changed by the business logic.
     */
    public val onSelectionsComputed: SubscriptionValue<SelectionsPerAttribute> = SubscriptionValue(mutableMapOf())

    /**
     * Mapping between a facet attribute and a facet values selection mode.
     * If not provided, the default selection mode is `single`.
     */
    public val selectionModeForAttribute: Map<Attribute, SelectionMode> = selectionModeForAttribute

    /**
     * Storage for selectable facet list logic per attribute.
     */
    private var facetListPerAttribute: MutableMap<Attribute, SelectableListViewModel<String, Facet>> = mutableMapOf()

    init {
        updateFacetLists()
    }

    /**
     * Returns a selection state of facet value for attribute.
     *
     * @param attribute facet attribute
     * @param facetValue facet value
     */
    public fun isSelected(attribute: Attribute, facetValue: String): Boolean {
        return selections[attribute]?.contains(facetValue) ?: false
    }

    /**
     * Toggle the selection state of facet value for attribute.
     *
     * @param attribute facet attribute
     * @param facetValue facet value
     */
    public fun toggleSelection(attribute: Attribute, facetValue: String) {
        facetListPerAttribute[attribute]?.select(facetValue)
    }

    /**
     * Update all facet lists.
     */
    private fun updateFacetLists() {
        orderedFacets.forEach { attributedFacet ->
            val attribute = attributedFacet.attribute
            val facetList = getOrCreateSelectableFacetList(attribute)
            facetList.items.value = attributedFacet.facets
            facetList.selections.value = selections[attribute] ?: emptySet()
        }
    }

    /**
     * Get or create a facet selectable list for a given attribute.
     */
    private fun getOrCreateSelectableFacetList(attribute: Attribute): SelectableListViewModel<String, Facet> {
        return facetListPerAttribute[attribute]
            ?: createFacetList(attribute).also { facetListPerAttribute[attribute] = it }
    }

    /**
     * Create a facet selectable list for a given attribute.
     */
    private fun createFacetList(attribute: Attribute): SelectableListViewModel<String, Facet> {
        val selectionMode = selectionModeForAttribute[attribute] ?: SelectionMode.Single
        val facetList = SelectableListViewModel<String, Facet>(selectionMode = selectionMode)
        facetList.eventSelection.subscribe { selection ->
            val currentSelections = selections.toMutableMap()
            currentSelections[attribute] = selection
            onSelectionsComputed.value = currentSelections
        }
        onSelectionsChanged.subscribe { selections ->
            facetList.selections.value = selections[attribute] ?: emptySet()
        }
        return facetList
    }
}
