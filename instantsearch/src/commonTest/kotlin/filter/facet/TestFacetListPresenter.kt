package filter.facet

import com.algolia.instantsearch.filter.facet.DefaultFacetListPresenter
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.AlphabeticalAscending
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.AlphabeticalDescending
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.CountAscending
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.CountDescending
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.IsRefined
import com.algolia.search.model.search.Facet
import shouldEqual
import kotlin.test.Test

class TestFacetListPresenter {

    private val facets = listOf(
        Facet("c", 0) to false,
        Facet("d", 1) to true,
        Facet("e", 3) to false,
        Facet("b", 3) to true,
        Facet("a", 4) to false
    )

    @Test
    fun alphaAsc() {
        DefaultFacetListPresenter(listOf(AlphabeticalAscending)).apply {
            invoke(facets) shouldEqual listOf(
                Facet("a", 4) to false,
                Facet("b", 3) to true,
                Facet("c", 0) to false,
                Facet("d", 1) to true,
                Facet("e", 3) to false
            )
        }
    }

    @Test
    fun alphaDesc() {
        DefaultFacetListPresenter(listOf(AlphabeticalDescending)).apply {
            invoke(facets) shouldEqual listOf(
                Facet("e", 3) to false,
                Facet("d", 1) to true,
                Facet("c", 0) to false,
                Facet("b", 3) to true,
                Facet("a", 4) to false
            )
        }
    }

    @Test
    fun countAsc() {
        DefaultFacetListPresenter(listOf(CountAscending)).apply {
            invoke(facets) shouldEqual listOf(
                Facet("c", 0) to false,
                Facet("d", 1) to true,
                Facet("e", 3) to false,
                Facet("b", 3) to true,
                Facet("a", 4) to false
            )
        }
    }

    @Test
    fun countDesc() {
        DefaultFacetListPresenter(listOf(CountDescending)).apply {
            invoke(facets) shouldEqual listOf(
                Facet("a", 4) to false,
                Facet("e", 3) to false,
                Facet("b", 3) to true,
                Facet("d", 1) to true,
                Facet("c", 0) to false
            )
        }
    }

    @Test
    fun isRefined() {
        DefaultFacetListPresenter(listOf(IsRefined)).apply {
            invoke(facets) shouldEqual listOf(
                Facet("d", 1) to true,
                Facet("b", 3) to true,
                Facet("c", 0) to false,
                Facet("e", 3) to false,
                Facet("a", 4) to false
            )
        }
    }

    @Test
    fun isRefinedThenAlphaAsc() {
        DefaultFacetListPresenter(listOf(IsRefined, AlphabeticalAscending)).apply {
            invoke(facets) shouldEqual listOf(
                Facet("b", 3) to true,
                Facet("d", 1) to true,
                Facet("a", 4) to false,
                Facet("c", 0) to false,
                Facet("e", 3) to false
            )
        }
    }

    @Test
    fun countDescThenIsRefined() {
        DefaultFacetListPresenter(listOf(CountDescending, IsRefined)).apply {
            invoke(facets) shouldEqual listOf(
                Facet("a", 4) to false,
                Facet("b", 3) to true,
                Facet("e", 3) to false,
                Facet("d", 1) to true,
                Facet("c", 0) to false
            )
        }
    }

    @Test
    fun firstAlphaIsFinal() {
        DefaultFacetListPresenter(listOf(AlphabeticalAscending, AlphabeticalDescending)).apply {
            invoke(facets) shouldEqual listOf(
                Facet("a", 4) to false,
                Facet("b", 3) to true,
                Facet("c", 0) to false,
                Facet("d", 1) to true,
                Facet("e", 3) to false
            )
        }
    }
}
