package selectable.facet

import selectable.facet.FacetSortCriterion.*


public class SelectableFacetsPresenter(
    val sortBy: List<FacetSortCriterion> = listOf(CountDescending),
    val limit: Int = 5
) : (List<SelectableFacet>) -> (List<SelectableFacet>) {

    private val comparator = Comparator<SelectableFacet> { (facetA, isSelectedA), (facetB, isSelectedB) ->
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

    override fun invoke(selectableItems: List<SelectableFacet>): List<SelectableFacet> {
        return selectableItems.sortedWith(comparator).take(limit)
    }
}