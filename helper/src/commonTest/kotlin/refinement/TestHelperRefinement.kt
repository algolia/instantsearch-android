package refinement

import algolia
import blocking
import com.algolia.search.filter.setFacets
import com.algolia.search.model.Attribute
import com.algolia.search.model.multipleindex.IndexQuery
import com.algolia.search.model.search.Facet
import com.algolia.search.model.search.Query
import index
import index2
import searcher.*
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
    fun searcherSingleQuery() {
        blocking {
            val attribute = Attribute("brand")
            val query = Query().apply { setFacets(attribute) }
            val searcher = SearcherSingleQuery(index, query)
            val model = RefinementModel<Facet>()
            val view = MockView()
            model.connectView(view)
            model.connectSearcherSingleQuery(searcher, attribute)

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
    fun searcherMultipleIndices() {
        blocking {
            val attribute1 = Attribute("brand")
            val attribute2 = Attribute("color")
            val query = Query().apply { setFacets(attribute1, attribute2) }
            val indexQuery0 = IndexQuery(index.indexName, query)
            val indexQuery1 = IndexQuery(index2.indexName, query)
            val searcher = SearcherMultipleIndices(algolia, listOf(indexQuery0, indexQuery1))
            searcher.listeners += {
                it.results[0].facets.keys.size shouldEqual 2
                it.results[1].facets.keys.size shouldEqual 2
                it.results[0].indexName shouldEqual indexQuery0.indexName
                it.results[1].indexName shouldEqual indexQuery1.indexName
            }

            searcher.search()
            searcher.completed?.await()
        }
    }

    @Test
    fun searcherForFacetValue() {
        blocking {
            val attribute = Attribute("name")
            val searcher = SearcherForFacetValue(index, attribute)

            val model = RefinementModel<Facet>()
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