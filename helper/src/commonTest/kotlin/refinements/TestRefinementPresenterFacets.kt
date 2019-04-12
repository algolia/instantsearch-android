package refinements

import com.algolia.search.model.search.Facet
import refinement.RefinementPresenterFacets
import refinement.SortCriterium
import shouldEqual
import kotlin.test.Test


class TestRefinementPresenterFacets {

    private val refinements = listOf(
        Facet("c", 0) to false,
        Facet("d", 1) to true,
        Facet("e", 3) to false,
        Facet("b", 3) to true,
        Facet("a", 4) to false
    )

    @Test
    fun alphaAsc() {
        RefinementPresenterFacets().apply {
            sortCriteria = listOf(SortCriterium.AlphabeticalAsc)
            data = refinements
            data shouldEqual listOf(
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
        RefinementPresenterFacets().apply {
            sortCriteria = listOf(SortCriterium.AlphabeticalDesc)
            data = refinements
            data shouldEqual listOf(
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
        RefinementPresenterFacets().apply {
            sortCriteria = listOf(SortCriterium.CountAsc)
            data = refinements
            data shouldEqual listOf(
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
        RefinementPresenterFacets().apply {
            sortCriteria = listOf(SortCriterium.CountDesc)
            data = refinements
            data shouldEqual listOf(
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
        RefinementPresenterFacets().apply {
            sortCriteria = listOf(SortCriterium.IsRefined)
            data = refinements
            data shouldEqual listOf(
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
        RefinementPresenterFacets().apply {
            sortCriteria = listOf(SortCriterium.IsRefined, SortCriterium.AlphabeticalAsc)
            data = refinements
            data shouldEqual listOf(
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
        RefinementPresenterFacets().apply {
            sortCriteria = listOf(SortCriterium.CountDesc, SortCriterium.IsRefined)
            data = refinements
            data shouldEqual listOf(
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
        RefinementPresenterFacets().apply {
            sortCriteria = listOf(SortCriterium.AlphabeticalAsc, SortCriterium.AlphabeticalDesc)
            data = refinements
            data shouldEqual listOf(
                Facet("a", 4) to false,
                Facet("b", 3) to true,
                Facet("c", 0) to false,
                Facet("d", 1) to true,
                Facet("e", 3) to false
            )
        }
    }
}