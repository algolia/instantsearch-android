package refinement

import com.algolia.search.model.search.Facet
import refinement.SortCriterion.*
import selection.SelectableItem
import selection.SelectionListPresenter
import kotlin.properties.Delegates


public class RefinementFacetsPresenter(
    sortBy: List<SortCriterion> = listOf(CountDescending),
    limit: Int = 10
) : SelectionListPresenter<Facet>(limit) {

    public var sortBy by Delegates.observable(sortBy) { _, _, _ ->
        values = computeValues(values)
    }

    override val comparator = Comparator<SelectableItem<Facet>> { (facetA, isSelectedA), (facetB, isSelectedB) ->
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
}