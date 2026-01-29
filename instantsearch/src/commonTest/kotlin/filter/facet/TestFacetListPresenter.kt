package filter.facet

import com.algolia.client.model.search.FacetHits
import com.algolia.instantsearch.filter.facet.DefaultFacetListPresenter
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.AlphabeticalAscending
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.AlphabeticalDescending
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.CountAscending
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.CountDescending
import com.algolia.instantsearch.filter.facet.FacetSortCriterion.IsRefined
import shouldEqual
import kotlin.test.Test

class TestFacetListPresenter {

    private val facets = listOf(
        FacetHits("c", "", 0) to false,
        FacetHits("d", "", 1) to true,
        FacetHits("e", "", 3) to false,
        FacetHits("b", "", 3) to true,
        FacetHits("a", "", 4) to false
    )

    @Test
    fun alphaAsc() {
        DefaultFacetListPresenter(listOf(AlphabeticalAscending)).apply {
            invoke(facets) shouldEqual listOf(
                FacetHits("a", "", 4) to false,
                FacetHits("b", "", 3) to true,
                FacetHits("c", "", 0) to false,
                FacetHits("d", "", 1) to true,
                FacetHits("e", "", 3) to false
            )
        }
    }

    @Test
    fun alphaDesc() {
        DefaultFacetListPresenter(listOf(AlphabeticalDescending)).apply {
            invoke(facets) shouldEqual listOf(
                FacetHits("e", "", 3) to false,
                FacetHits("d", "", 1) to true,
                FacetHits("c", "", 0) to false,
                FacetHits("b", "", 3) to true,
                FacetHits("a", "", 4) to false
            )
        }
    }

    @Test
    fun countAsc() {
        DefaultFacetListPresenter(listOf(CountAscending)).apply {
            invoke(facets) shouldEqual listOf(
                FacetHits("c", "", 0) to false,
                FacetHits("d", "", 1) to true,
                FacetHits("e", "", 3) to false,
                FacetHits("b", "", 3) to true,
                FacetHits("a", "", 4) to false
            )
        }
    }

    @Test
    fun countDesc() {
        DefaultFacetListPresenter(listOf(CountDescending)).apply {
            invoke(facets) shouldEqual listOf(
                FacetHits("a", "", 4) to false,
                FacetHits("e", "", 3) to false,
                FacetHits("b", "", 3) to true,
                FacetHits("d", "", 1) to true,
                FacetHits("c", "", 0) to false
            )
        }
    }

    @Test
    fun isRefined() {
        DefaultFacetListPresenter(listOf(IsRefined)).apply {
            invoke(facets) shouldEqual listOf(
                FacetHits("d", "", 1) to true,
                FacetHits("b", "", 3) to true,
                FacetHits("c", "", 0) to false,
                FacetHits("e", "", 3) to false,
                FacetHits("a", "", 4) to false
            )
        }
    }

    @Test
    fun isRefinedThenAlphaAsc() {
        DefaultFacetListPresenter(listOf(IsRefined, AlphabeticalAscending)).apply {
            invoke(facets) shouldEqual listOf(
                FacetHits("b", "", 3) to true,
                FacetHits("d", "", 1) to true,
                FacetHits("a", "", 4) to false,
                FacetHits("c", "", 0) to false,
                FacetHits("e", "", 3) to false
            )
        }
    }

    @Test
    fun countDescThenIsRefined() {
        DefaultFacetListPresenter(listOf(CountDescending, IsRefined)).apply {
            invoke(facets) shouldEqual listOf(
                FacetHits("a", "", 4) to false,
                FacetHits("b", "", 3) to true,
                FacetHits("e", "", 3) to false,
                FacetHits("d", "", 1) to true,
                FacetHits("c", "", 0) to false
            )
        }
    }

    @Test
    fun firstAlphaIsFinal() {
        DefaultFacetListPresenter(listOf(AlphabeticalAscending, AlphabeticalDescending)).apply {
            invoke(facets) shouldEqual listOf(
                FacetHits("a", "", 4) to false,
                FacetHits("b", "", 3) to true,
                FacetHits("c", "", 0) to false,
                FacetHits("d", "", 1) to true,
                FacetHits("e", "", 3) to false
            )
        }
    }
}
