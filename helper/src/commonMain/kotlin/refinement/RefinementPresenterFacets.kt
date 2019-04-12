package refinement

import com.algolia.search.model.search.Facet
import refinement.SortCriterium.*
import kotlin.properties.Delegates


class RefinementPresenterFacets : RefinementPresenter<Facet>() {

    var sortCriteria by Delegates.observable(listOf(IsRefined, CountDesc)) { _, _, _ ->
        data = data
    }

    override val comparator = Comparator<RefinedData<Facet>> { (facetA, isSelectedA), (facetB, isSelectedB) ->
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