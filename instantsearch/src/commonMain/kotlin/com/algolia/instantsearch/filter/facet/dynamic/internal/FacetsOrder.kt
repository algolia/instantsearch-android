package com.algolia.instantsearch.filter.facet.dynamic.internal

import com.algolia.client.model.search.FacetHits
import com.algolia.client.model.search.FacetOrdering
import com.algolia.client.model.search.SortRemainingBy
import com.algolia.instantsearch.filter.facet.dynamic.AttributedFacets

/**
 * Apply the algorithm transforming the received facets and facet ordering rules to the list of ordered facet attributes
 * and ordered values.
 *
 * @param facets facets per attribute
 * @param facetOrdering facets ordering rule
 */
internal fun facetsOrder(facets: Map<String, List<FacetHits>>, facetOrdering: FacetOrdering): List<AttributedFacets> {
    val orderedAttributes = facetOrdering.facets?.order?.mapNotNull { attribute -> facets.keys.firstOrNull { it == attribute } }
    return orderedAttributes?.map { attribute ->
        val facetValues = facets[attribute] ?: emptyList()
        val orderedFacetValues = facetOrdering.values?.get(attribute)?.let { order(facetValues, it) } ?: facetValues
        AttributedFacets(attribute, orderedFacetValues)
    } ?: emptyList()
}

/**
 * Order facet values.
 *
 * @param facets the list of facets to order
 * @param rule the ordering rule for facets
 * @return list of ordered facets
 */
private fun order(facets: List<FacetHits>, rule: com.algolia.client.model.search.Value): List<FacetHits> {
    if (facets.size <= 1) return facets
    val pinnedFacets = rule.order?.mapNotNull { value -> facets.firstOrNull { it.value == value } } ?: listOf()
    val remainingFacets = facets.filter { !pinnedFacets.contains(it) }
    val facetsTail: List<FacetHits> = when (rule.sortRemainingBy ?: SortRemainingBy.Count) {
        SortRemainingBy.Alpha -> remainingFacets.sortedBy { it.value }
        SortRemainingBy.Count -> remainingFacets.sortedByDescending { it.count }
        SortRemainingBy.Hidden -> emptyList()
    }
    return pinnedFacets + facetsTail
}
