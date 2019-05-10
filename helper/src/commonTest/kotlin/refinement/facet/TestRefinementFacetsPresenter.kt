package refinement.facet

import com.algolia.search.model.search.Facet
import refinement.facet.RefinementFacetSortCriterion.*
import shouldEqual
import kotlin.test.Test


class TestRefinementFacetsPresenter {

    private val facets = listOf(
        Facet("c", 0) to false,
        Facet("d", 1) to true,
        Facet("e", 3) to false,
        Facet("b", 3) to true,
        Facet("a", 4) to false
    )

    @Test
    fun alphaAsc() {
        RefinementFacetsPresenter(listOf(AlphabeticalAscending)).apply {
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
        RefinementFacetsPresenter(listOf(AlphabeticalDescending)).apply {
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
        RefinementFacetsPresenter(listOf(CountAscending)).apply {
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
        RefinementFacetsPresenter(listOf(CountDescending)).apply {
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
        RefinementFacetsPresenter(listOf(IsRefined)).apply {
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
        RefinementFacetsPresenter(listOf(IsRefined, AlphabeticalAscending)).apply {
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
        RefinementFacetsPresenter(listOf(CountDescending, IsRefined)).apply {
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
        RefinementFacetsPresenter(listOf(AlphabeticalAscending, AlphabeticalDescending)).apply {
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