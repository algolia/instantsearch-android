package refinement

import com.algolia.search.model.search.Facet
import refinement.RefinementPresenterFacets.SortCriterium.*
import kotlin.properties.Delegates


class RefinementPresenterFacets : RefinementPresenter<Facet>() {

    enum class SortCriterium {
        IsRefined,
        CountAsc,
        CountDesc,
        AlphabeticalAsc,
        AlphabeticalDesc
    }

    var sortCriteria by Delegates.observable(setOf(IsRefined, CountDesc)) { _, _, _ ->
        data = data
    }

    override val comparator = Comparator<RefinedData<Facet>> { (facetA, isSelectedA), (facetB, isSelectedB) ->
        sortCriteria.asSequence().map {
            when (it) {
                CountAsc -> facetA.count.compareTo(facetB.count)
                CountDesc -> facetB.count.compareTo(facetA.count)
                AlphabeticalAsc -> facetA.value.compareTo(facetB.value)
                AlphabeticalDesc -> facetB.value.compareTo(facetA.value)
                IsRefined -> isSelectedB.compareTo(isSelectedA)
            }
        }.first { it != 0 }
    }
}