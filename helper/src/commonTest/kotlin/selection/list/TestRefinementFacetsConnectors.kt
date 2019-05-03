package selection.list

import blocking
import com.algolia.search.model.filter.FilterConverter
import filter.FilterGroupID
import filter.toFilter
import refinement.facet.RefinementFacetsViewModel
import refinement.RefinementOperator
import selection.TestRefinementConnectors
import refinement.facet.connectSearcher
import search.SearcherSingleIndex
import shouldBeEmpty
import shouldEqual
import kotlin.test.Test


class TestRefinementFacetsConnectors : TestRefinementConnectors() {
    @Test
    fun connectWithSSI() {
        blocking {
            val searcher = SearcherSingleIndex(mockIndex)
            val model = RefinementFacetsViewModel()
            val facet = facets.first()
            val filter = facet.toFilter(color)

            model.connectSearcher(color, searcher, RefinementOperator.And)
            searcher.search()
            searcher.sequencer.currentOperation.join()
            model.items.toSet() shouldEqual facets.toSet()
            model.selections.shouldBeEmpty()
            model.selectItem(facet.value)
            searcher.sequencer.currentOperation.join()
            model.selections shouldEqual setOf(facet.value)
            searcher.query.filters = FilterConverter.SQL(filter)
            searcher.filterState.getFacetFilters(FilterGroupID.And(color.raw))!! shouldEqual setOf(filter)
        }
    }

    @Test
    fun modelReactsToFilterStateChanges() {
        blocking {
            val searcher = SearcherSingleIndex(mockIndex)
            val model = RefinementFacetsViewModel()
            val facet = facets.first()

            model.connectSearcher(color, searcher, RefinementOperator.And)
            searcher.search()
            searcher.sequencer.currentOperation.join()
            model.selections.shouldBeEmpty()
            searcher.filterState.notify {
                add(FilterGroupID.And(color.raw), facet.toFilter(color))
            }
            model.selections shouldEqual setOf(facet.value)
        }
    }
}