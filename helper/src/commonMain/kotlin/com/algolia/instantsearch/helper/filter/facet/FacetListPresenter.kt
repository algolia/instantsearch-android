package com.algolia.instantsearch.helper.filter.facet

import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion.*


public class FacetListPresenter(
    private val sortBy: List<FacetSortCriterion> = listOf(CountDescending),
    private val limit: Int = 5
) : (List<FacetListItem>) -> (List<FacetListItem>) {

    private val comparator = Comparator<FacetListItem> { (facetA, isSelectedA), (facetB, isSelectedB) ->
        sortBy.asSequence().distinct().map {
            when (it) {
                CountAscending -> facetA.count.compareTo(facetB.count)
                CountDescending -> facetB.count.compareTo(facetA.count)
                AlphabeticalAscending -> facetA.value.compareTo(facetB.value)
                AlphabeticalDescending -> facetB.value.compareTo(facetA.value)
                IsRefined -> isSelectedB.compareTo(isSelectedA)
            }
        }.firstOrNull { it != 0 } ?: 0
    }

    override fun invoke(selectableItems: List<FacetListItem>): List<FacetListItem> {
        return selectableItems.sortedWith(comparator).take(limit)
    }
}