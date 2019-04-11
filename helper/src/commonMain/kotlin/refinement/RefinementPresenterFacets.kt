package refinement

import com.algolia.search.model.search.Facet


class RefinementPresenterFacets : RefinementPresenter<Facet>() {

    override val sortComparator = Comparator<RefinedData<Facet>> { (facetA, isSelectedA), (facetB, isSelectedB) ->
        sortCriteria.asSequence().map {
            when (it) {
                SortCriterium.CountAsc -> facetA.count.compareTo(facetB.count)
                SortCriterium.CountDesc -> facetB.count.compareTo(facetA.count)
                SortCriterium.AlphabeticalAsc -> facetA.value.compareTo(facetB.value)
                SortCriterium.AlphabeticalDesc -> facetB.value.compareTo(facetA.value)
                SortCriterium.IsRefined -> isSelectedB.compareTo(isSelectedA)
            }
        }.first { it != 0 }
    }
}