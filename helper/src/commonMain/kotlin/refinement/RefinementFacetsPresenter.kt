package refinement

import com.algolia.search.model.search.Facet
import refinement.SortCriterion.*
import selection.SelectableItem


class RefinementFacetsPresenter(
    val sortBy: List<SortCriterion> = listOf(CountDescending),
    val limit: Int = 5
) : (List<SelectableItem<Facet>>) -> (List<SelectableItem<Facet>>) {

    private val comparator = Comparator<SelectableItem<Facet>> { (facetA, isSelectedA), (facetB, isSelectedB) ->
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

    override fun invoke(selectableItems: List<SelectableItem<Facet>>): List<SelectableItem<Facet>> {
        return selectableItems.sortedWith(comparator).take(limit)
    }
}