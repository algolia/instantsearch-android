package com.algolia.instantsearch.filter.facet.dynamic.internal

import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets
import com.algolia.search.model.Attribute
import com.algolia.search.model.rule.FacetOrdering
import com.algolia.search.model.rule.FacetValuesOrder
import com.algolia.search.model.rule.SortRule
import com.algolia.search.model.search.Facet

/**
 * Apply the algorithm transforming the received facets and facet ordering rules to the list of ordered facet attributes
 * and ordered values.
 *
 * @param facets facets per attribute
 * @param facetOrdering facets ordering rule
 */
internal fun facetsOrder(facets: Map<Attribute, List<Facet>>, facetOrdering: FacetOrdering): List<AttributedFacets> {
    val orderedAttributes = facetOrdering.facets.order.mapNotNull { attribute -> facets.keys.firstOrNull { it.raw == attribute } }
    return orderedAttributes.map { attribute ->
        val facetValues = facets[attribute] ?: emptyList()
        val orderedFacetValues = facetOrdering.values[attribute]?.let { order(facetValues, it) } ?: facetValues
        AttributedFacets(attribute, orderedFacetValues)
    }
}

/**
 * Order facet values.
 *
 * @param facets the list of facets to order
 * @param rule the ordering rule for facets
 * @return list of ordered facets
 */
private fun order(facets: List<Facet>, rule: FacetValuesOrder): List<Facet> {
    if (facets.size <= 1) return facets
    val pinnedFacets = rule.order.mapNotNull { value -> facets.firstOrNull { it.value == value } }
    val remainingFacets = facets.filter { !pinnedFacets.contains(it) }
    val facetsTail: List<Facet> = when (rule.sortRemainingBy ?: SortRule.Count) {
        SortRule.Alpha -> remainingFacets.sortedBy { it.value }
        SortRule.Count -> remainingFacets.sortedByDescending { it.count }
        SortRule.Hidden -> emptyList()
    }
    return pinnedFacets + facetsTail
}
