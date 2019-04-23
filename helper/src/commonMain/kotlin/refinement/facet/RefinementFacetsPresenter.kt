package refinement.facet

import refinement.facet.FacetSortCriterion.*


class RefinementFacetsPresenter(
    val sortBy: List<FacetSortCriterion> = listOf(CountDescending),
    val limit: Int = 5
) : (List<RefinementFacet>) -> (List<RefinementFacet>) {

    private val comparator = Comparator<RefinementFacet> { (facetA, isSelectedA), (facetB, isSelectedB) ->
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

    override fun invoke(selectableItems: List<RefinementFacet>): List<RefinementFacet> {
        return selectableItems.sortedWith(comparator).take(limit)
    }
}