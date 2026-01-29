package com.algolia.instantsearch.filter.facet.dynamic

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.core.selectable.list.SelectableListViewModel
import com.algolia.instantsearch.core.selectable.list.SelectionMode
import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.instantsearch.core.subscription.SubscriptionValue
import com.algolia.instantsearch.extension.traceDynamicFacet
import kotlin.properties.Delegates
import kotlinx.serialization.InternalSerializationApi

/**
 * Dynamic facets business logic.
 * Provides an ordered list of attributed facets, facet selections and events.
 *
 * @param orderedFacets ordered list of attributed facets
 * @param selections mapping between a facet attribute and a set of selected facet values
 * @param selectionModeForAttribute Mapping between a facet attribute and a facet values selection mode
 * @param defaultSelectionMode selection mode to apply for a facet list
 */
public class DynamicFacetListViewModel(
    orderedFacets: List<AttributedFacets> = emptyList(),
    selections: SelectionsPerAttribute = mutableMapOf<String, Set<String>>(),
    selectionModeForAttribute: Map<String, SelectionMode> = emptyMap(),
    /**
     * Selection mode to apply for a facet list..
     */
    public val defaultSelectionMode: SelectionMode = SelectionMode.Single,
) {

    init {
        traceDynamicFacet(orderedFacets, selections, selectionModeForAttribute)
    }

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
    public val onSelectionsComputed: SubscriptionValue<SelectionsPerAttribute> = SubscriptionValue(mutableMapOf<String, Set<String>>())

    /**
     * Mapping between a facet attribute and a facet values selection mode.
     * If not provided, the default selection mode is `single`.
     */
    public val selectionModeForAttribute: Map<String, SelectionMode> = selectionModeForAttribute

    /**
     * Storage for selectable facet list logic per attribute.
     */
    @OptIn(InternalSerializationApi::class)
    private var facetListPerAttribute: MutableMap<String, SelectableListViewModel<String, FacetHits>> = mutableMapOf()

    init {
        updateFacetLists()
    }

    /**
     * Returns a selection state of facet value for attribute.
     *
     * @param attribute facet attribute
     * @param facetValue facet value
     */
    public fun isSelected(attribute: String, facetValue: String): Boolean {
        return selections[attribute]?.contains(facetValue) ?: false
    }

    /**
     * Toggle the selection state of facet value for attribute.
     *
     * @param attribute facet attribute
     * @param facetValue facet value
     */
    @OptIn(InternalSerializationApi::class)
    public fun toggleSelection(attribute: String, facetValue: String) {
        facetListPerAttribute[attribute]?.select(facetValue)
    }

    /**
     * Update all facet lists.
     */
    @OptIn(InternalSerializationApi::class)
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
    @OptIn(InternalSerializationApi::class)
    private fun getOrCreateSelectableFacetList(attribute: String): SelectableListViewModel<String, FacetHits> {
        return facetListPerAttribute[attribute]
            ?: createFacetList(attribute).also { facetListPerAttribute[attribute] = it }
    }

    /**
     * Create a facet selectable list for a given attribute.
     */
    @OptIn(InternalSerializationApi::class)
    private fun createFacetList(attribute: String): SelectableListViewModel<String, FacetHits> {
        val selectionMode = selectionModeForAttribute[attribute] ?: defaultSelectionMode
        val facetList = SelectableListViewModel<String, FacetHits>(selectionMode = selectionMode)
        facetList.eventSelection.subscribe { selection ->
            val currentSelections = selections.toMutableMap()
            currentSelections[attribute as String] = selection
            onSelectionsComputed.value = currentSelections
        }
        onSelectionsChanged.subscribe { selections ->
            facetList.selections.value = selections[attribute] ?: emptySet()
        }
        return facetList
    }
}
