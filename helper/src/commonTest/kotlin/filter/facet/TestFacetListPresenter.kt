package filter.facet

import com.algolia.instantsearch.helper.filter.facet.FacetListPresenter
import com.algolia.instantsearch.helper.filter.facet.FacetSortCriterion.*
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
        FacetListPresenter(listOf(AlphabeticalAscending)).apply {
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
        FacetListPresenter(listOf(AlphabeticalDescending)).apply {
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
        FacetListPresenter(listOf(CountAscending)).apply {
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
        FacetListPresenter(listOf(CountDescending)).apply {
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
        FacetListPresenter(listOf(IsRefined)).apply {
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
        FacetListPresenter(listOf(IsRefined, AlphabeticalAscending)).apply {
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
        FacetListPresenter(listOf(CountDescending, IsRefined)).apply {
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
        FacetListPresenter(listOf(AlphabeticalAscending, AlphabeticalDescending)).apply {
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