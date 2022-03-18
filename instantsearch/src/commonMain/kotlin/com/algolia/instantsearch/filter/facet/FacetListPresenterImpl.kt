package com.algolia.instantsearch.filter.facet

public class FacetListPresenterImpl(
    public val sortBy: List<FacetSortCriterion> = listOf(FacetSortCriterion.CountDescending),
    public val limit: Int = 5,
) : FacetListPresenter {

    private val comparator = Comparator<FacetListItem> { (facetA, isSelectedA), (facetB, isSelectedB) ->
        sortBy.asSequence().distinct().map {
            when (it) {
                FacetSortCriterion.CountAscending -> facetA.count.compareTo(facetB.count)
                FacetSortCriterion.CountDescending -> facetB.count.compareTo(facetA.count)
                FacetSortCriterion.AlphabeticalAscending -> facetA.value.compareTo(facetB.value)
                FacetSortCriterion.AlphabeticalDescending -> facetB.value.compareTo(facetA.value)
                FacetSortCriterion.IsRefined -> isSelectedB.compareTo(isSelectedA)
            }
        }.firstOrNull { it != 0 } ?: 0
    }

    override fun invoke(selectableItems: List<FacetListItem>): List<FacetListItem> {
        return selectableItems.sortedWith(comparator).take(limit)
    }
}
