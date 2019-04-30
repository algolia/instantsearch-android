package refinement.facet

import blocking
import com.algolia.search.model.filter.FilterConverter
import filter.FilterGroupID
import filter.toFilter
import refinement.RefinementOperator
import refinement.TestRefinementConnectors
import search.SearcherSingleIndex
import shouldEqual
import kotlin.test.Test

class TestRefinementFilterConnectors : TestRefinementConnectors() {

    @Test
    fun connectWithSSI() {
        blocking {
            val searcher = SearcherSingleIndex(mockIndex)
            val facet = facets.first()
            val filter = facet.toFilter(color)
            val model = RefinementFilterViewModel(filter)

            model.connectSearcher(searcher, RefinementOperator.And)
            searcher.search()
            searcher.sequencer.currentOperation.join()
            model.item shouldEqual filter
            model.selected shouldEqual false

            model.toggleSelection()
            searcher.sequencer.currentOperation.join()
            model.selected shouldEqual true
            searcher.query.filters = FilterConverter.SQL(filter)
            searcher.filterState.getFacets(FilterGroupID.And(color))!! shouldEqual setOf(filter)
        }
    }

    @Test
    fun modelReactsToFilterStateChanges() {
        blocking {
            val searcher = SearcherSingleIndex(mockIndex)
            val facet = facets.first()
            val filter = facet.toFilter(color)
            val model = RefinementFilterViewModel(filter)

            model.connectSearcher(searcher, RefinementOperator.And)
            searcher.search()
            searcher.sequencer.currentOperation.join()
            model.selected shouldEqual false
            searcher.filterState.notify {
                add(FilterGroupID.And(color), facet.toFilter(color))
            }
            model.selected shouldEqual true
        }
    }
}

