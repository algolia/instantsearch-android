package refinement

import blocking
import com.algolia.search.filter.setFacets
import com.algolia.search.model.Attribute
import com.algolia.search.model.search.Facet
import com.algolia.search.model.search.Query
import indexA
import searcher.SearcherForFacetValue
import searcher.SearcherSingleIndex
import searcher.connectSearcherForFacetValue
import searcher.connectSearcherSingleIndex
import shouldBeEmpty
import shouldEqual
import shouldNotBeEmpty
import kotlin.test.Test


class TestHelperRefinement {

    private class MockView : RefinementView<Facet> {

        lateinit var click: (Facet) -> Unit
        var data = listOf<Facet>()
        var dataSelected = listOf<Facet>()

        override fun setRefinements(refinements: List<Facet>) {
            data = refinements
        }

        override fun setSelected(refinements: List<Facet>) {
            dataSelected = refinements
        }

        override fun onClickRefinement(onClick: (Facet) -> Unit) {
            click = { refinement: Facet ->
                onClick(refinement)
            }
        }
    }

    @Test
    fun withSearcherSingleIndex() {
        blocking {
            val attribute = Attribute("brand")
            val query = Query().apply { setFacets(attribute) }
            val searcher = SearcherSingleIndex(indexA, query)
            val model = RefinementListViewModel<Facet>()
            val view = MockView()
            model.connectView(view)
            model.connectSearcherSingleIndex(searcher, attribute)

            searcher.search()
            searcher.completed?.await()
            model.refinements.size shouldEqual 2
            query.filterBuilder.get().shouldBeEmpty()

            view.click(model.refinements.first())
            searcher.completed?.await()
            query.filterBuilder.get().shouldNotBeEmpty()
            model.refinements.size shouldEqual 1
        }
    }

    @Test
    fun withSearcherForFacetValue() {
        blocking {
            val attribute = Attribute("name")
            val searcher = SearcherForFacetValue(indexA, attribute)

            val model = RefinementListViewModel<Facet>()
            val view = MockView()

            model.connectView(view)
            model.connectSearcherForFacetValue(searcher)
            searcher.search()
            searcher.completed?.await()
            model.refinements.size shouldEqual 2
            view.click(model.refinements.first())
            searcher.completed?.await()
            model.refinements.size shouldEqual 2
        }
    }
}
