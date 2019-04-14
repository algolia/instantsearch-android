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
        RefinementFacetsPresenter(listOf(SortCriterion.AlphabeticalAsc)).apply {
            refinements = facets
            refinements shouldEqual listOf(
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
        RefinementFacetsPresenter(listOf(SortCriterion.AlphabeticalDesc)).apply {
            refinements = facets
            refinements shouldEqual listOf(
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
        RefinementFacetsPresenter(listOf(SortCriterion.CountAsc)).apply {
            refinements = facets
            refinements shouldEqual listOf(
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
        RefinementFacetsPresenter(listOf(SortCriterion.CountDesc)).apply {
            refinements = facets
            refinements shouldEqual listOf(
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
        RefinementFacetsPresenter(listOf(SortCriterion.IsRefined)).apply {
            refinements = facets
            refinements shouldEqual listOf(
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
        RefinementFacetsPresenter(listOf(SortCriterion.IsRefined, SortCriterion.AlphabeticalAsc)).apply {
            refinements = facets
            refinements shouldEqual listOf(
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
        RefinementFacetsPresenter(listOf(SortCriterion.CountDesc, SortCriterion.IsRefined)).apply {
            refinements = facets
            refinements shouldEqual listOf(
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
        RefinementFacetsPresenter(listOf(SortCriterion.AlphabeticalAsc, SortCriterion.AlphabeticalDesc)).apply {
            refinements = facets
            refinements shouldEqual listOf(
                Facet("a", 4) to false,
                Facet("b", 3) to true,
                Facet("c", 0) to false,
                Facet("d", 1) to true,
                Facet("e", 3) to false
            )
        }
    }
}