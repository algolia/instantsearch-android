package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.search.model.Attribute
import com.algolia.search.model.rule.AttributedFacets
import com.algolia.search.model.search.Facet
import kotlin.properties.Delegates

public class DynamicFacetViewModel(
    facetOrder: List<AttributedFacets>,
    selections: FacetSelections,
) {

    public var facetOrder: List<AttributedFacets> by Delegates.observable(facetOrder) { _, _, newValue ->
        onFacetOrderUpdated.send(prepareFacetOrder(newValue))
    }
    public var selections: FacetSelections by Delegates.observable(selections) { _, oldValue, newValue ->
        onSelectionsUpdated.send(newValue)
    }
    public var shouldShowFacetForAttribute: (Attribute, Facet) -> (Boolean) = { _, _ -> true }

    public val onFacetOrderUpdated: SubscriptionEvent<List<AttributedFacets>> = SubscriptionEvent()
    public val onSelectionsUpdated: SubscriptionEvent<FacetSelections> = SubscriptionEvent()

    private fun prepareFacetOrder(facetOrder: List<AttributedFacets>): List<AttributedFacets> {
        return facetOrder.map { attributedFacets ->
            val filteredFacets = attributedFacets.facets.filter { facet ->
                shouldShowFacetForAttribute(attributedFacets.attribute, facet)
            }
            AttributedFacets(attribute = attributedFacets.attribute, facets = filteredFacets)
        }.filter { it.facets.isNotEmpty() }
    }

    public fun isSelected(attribute: Attribute, facetValue: String): Boolean {
        return selections[attribute]?.contains(facetValue) ?: false
    }

    public fun toggleSelection(attribute: Attribute, facetValue: String) {
        val current = selections[attribute]?.toMutableSet() ?: mutableSetOf()
        current.setFacetValue(facetValue)
        current.setSelection(attribute)
    }

    private fun MutableSet<String>.setFacetValue(facetValue: String) {
        if (contains(facetValue)) remove(facetValue) else add(facetValue)
    }

    private fun MutableSet<String>.setSelection(attribute: Attribute) {
        selections = if (isEmpty()) selections - attribute else selections + (attribute to this)
    }
}
