package selection.facet

import blocking
import com.algolia.search.model.filter.FilterConverter
import filter.FilterGroupID
import filter.toFilter
import refinement.RefinementOperator
import refinement.filter.RefinementFilterViewModel
import refinement.filter.connectSearcher
import search.SearcherSingleIndex
import selection.TestRefinementConnectors
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
            model.isSelected shouldEqual false

            model.computeIsSelected(true)
            searcher.sequencer.currentOperation.join()
            model.isSelected shouldEqual true
            searcher.query.filters = FilterConverter.SQL(filter)
            searcher.filterState.getFacetFilters(FilterGroupID.And(color))!! shouldEqual setOf(filter)
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
            model.isSelected shouldEqual false
            searcher.filterState.notify {
                add(FilterGroupID.And(color), facet.toFilter(color))
            }
            model.isSelected shouldEqual true
        }
    }
}

