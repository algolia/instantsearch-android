package com.algolia.instantsearch.helper.filter.facet

import kotlin.jvm.JvmField

/**
 * A default Presenter for facet list items, sorted with [comparator] and displayed until [limit].
 */
public class FacetListPresenterImpl(
    @JvmField public val sortBy: List<FacetSortCriterion> = listOf(FacetSortCriterion.CountDescending),
    @JvmField public val limit: Int = 5
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
        return present(selectableItems)
    }

    /**
     * Presents the given items as a list of selectable items.
     */
    public fun present(selectableItems: List<FacetListItem>) =
        selectableItems.sortedWith(comparator).take(limit)
}