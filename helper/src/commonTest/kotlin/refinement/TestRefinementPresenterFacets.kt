package refinement

import com.algolia.search.model.search.Facet
import shouldEqual
import kotlin.test.Test


class TestRefinementPresenterFacets {

    private val facets = listOf(
        Facet("c", 0) to false,
        Facet("d", 1) to true,
        Facet("e", 3) to false,
        Facet("b", 3) to true,
        Facet("a", 4) to false
    )

    @Test
    fun alphaAsc() {
        RefinementFacetsPresenter(listOf(FacetSortCriterion.AlphabeticalAscending)).apply {
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
        RefinementFacetsPresenter(listOf(FacetSortCriterion.AlphabeticalDescending)).apply {
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
        RefinementFacetsPresenter(listOf(FacetSortCriterion.CountAscending)).apply {
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
        RefinementFacetsPresenter(listOf(FacetSortCriterion.CountDescending)).apply {
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
        RefinementFacetsPresenter(listOf(FacetSortCriterion.IsRefined)).apply {
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
        RefinementFacetsPresenter(listOf(FacetSortCriterion.IsRefined, FacetSortCriterion.AlphabeticalAscending)).apply {
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
        RefinementFacetsPresenter(listOf(FacetSortCriterion.CountDescending, FacetSortCriterion.IsRefined)).apply {
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
        RefinementFacetsPresenter(listOf(FacetSortCriterion.AlphabeticalAscending, FacetSortCriterion.AlphabeticalDescending)).apply {
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