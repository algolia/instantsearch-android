package com.algolia.instantsearch.helper.filter.facet.dynamic

import com.algolia.instantsearch.core.subscription.SubscriptionEvent
import com.algolia.search.model.Attribute
import com.algolia.search.model.rule.AttributedFacets
import com.algolia.search.model.rule.FacetMerchandising
import com.algolia.search.model.search.Facet
import kotlin.properties.Delegates

public class DynamicFacetViewModel(
    facetOrder: FacetMerchandising,
    selections: FacetSelections,
) {

    public var facetOrder: FacetMerchandising by Delegates.observable(facetOrder) { _, _, newValue ->
        onFacetOrderUpdated.send(FacetMerchandising(prepareFacetOrder(newValue)))
    }
    public var selections: FacetSelections by Delegates.observable(selections) { _, _, newValue ->
        onSelectionsUpdated.send(newValue)
    }
    public var shouldShowFacetForAttribute: (Attribute, Facet) -> (Boolean) = { _, _ -> true }

    public val onFacetOrderUpdated: SubscriptionEvent<FacetMerchandising> = SubscriptionEvent()
    public val onSelectionsUpdated: SubscriptionEvent<FacetSelections> = SubscriptionEvent()

    private fun prepareFacetOrder(facetMerchandising: FacetMerchandising): List<AttributedFacets> {
        return facetMerchandising.facetOrder.map { attributedFacets ->
            val filteredFacets = attributedFacets.facets.filter { facet ->
                shouldShowFacetForAttribute(attributedFacets.attribute, facet)
            }
            AttributedFacets(attribute = attributedFacets.attribute, facets = filteredFacets)
        }.filter { it.facets.isNotEmpty() }
    }

    public fun isSelected(facetValue: String, attribute: Attribute): Boolean {
        return selections[attribute]?.contains(facetValue) ?: false
    }

    public fun toggleSelection(facetValue: String, attribute: Attribute) {
        val currentSelections = selections[attribute] ?: mutableSetOf()
        currentSelections.run {
            if (contains(facetValue)) remove(facetValue) else add(facetValue)
            if (isEmpty()) selections.remove(attribute) else selections[attribute] = currentSelections
        }
    }
}
