package refinement

import com.algolia.search.model.search.Facet


class RefinementPresenterFacets() : RefinementPresenter<Facet>() {

    override val sortComparator: Comparator<RefinedData<Facet>> = Comparator { a, b ->
        val (facetA, isRefinedA) = a
        val (facetB, isRefinedB) = b

        sortOrder.asSequence().map {
            when (it) {
                SortCriteria.CountAsc -> facetA.count.compareTo(facetB.count)
                SortCriteria.CountDesc -> facetB.count.compareTo(facetA.count)
                SortCriteria.AlphabeticalAsc -> facetA.value.compareTo(facetB.value)
                SortCriteria.AlphabeticalDesc -> facetB.value.compareTo(facetA.value)
                SortCriteria.IsRefined -> isRefinedB.compareTo(isRefinedA)
            }
        }.first { it != 0 }
    }

    override fun sortData(data: List<RefinedData<Facet>>): List<RefinedData<Facet>> {
        return data.sortedWith(sortComparator)
    }
}