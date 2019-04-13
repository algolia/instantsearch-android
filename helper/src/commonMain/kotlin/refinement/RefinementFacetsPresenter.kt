package refinement

import com.algolia.search.model.search.Facet
import refinement.SortCriterium.*
import kotlin.properties.Delegates


public class RefinementFacetsPresenter(
    sortCriteria: List<SortCriterium> = listOf(AlphabeticalAsc),
    limit: Int = 10
) : RefinementListPresenter<Facet>(limit) {

    public var sortCriteria by Delegates.observable(sortCriteria) { _, _, _ ->
        refinements = refinements
    }

    override val comparator = Comparator<SelectedRefinement<Facet>> { (facetA, isSelectedA), (facetB, isSelectedB) ->
        sortCriteria.asSequence().distinct().map {
            when (it) {
                CountAsc -> facetA.count.compareTo(facetB.count)
                CountDesc -> facetB.count.compareTo(facetA.count)
                AlphabeticalAsc -> facetA.value.compareTo(facetB.value)
                AlphabeticalDesc -> facetB.value.compareTo(facetA.value)
                IsRefined -> isSelectedB.compareTo(isSelectedA)
            }
        }.firstOrNull { it != 0 } ?: 0
    }
}