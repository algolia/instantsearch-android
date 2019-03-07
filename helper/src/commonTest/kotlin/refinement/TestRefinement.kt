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


class TestRefinement {

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
    fun searcherMultiQuerySingleQuery() {
        blocking {
            // Query1-> index1, index2
            // -> test widget1 displays refinements for 1, widget2 displays refinements for 2

            // GIVEN A QUERY MATCHING TWO RECORDS
            val attribute1 = Attribute("brand")
            val attribute2 = Attribute("color")
            val query = Query().apply { setFacets(attribute1, attribute2) }
            val indexQuery1 = IndexQuery(index.indexName, query)
            val indexQuery2 = IndexQuery(index2.indexName, query)
            val searcher = SearcherMultiQuery(algolia, listOf(indexQuery1, indexQuery2))
            val model1 = RefinementModel<Facet>()
            val model2 = RefinementModel<Facet>()
            val view1 = MockView()
            val view2 = MockView()

            // GIVEN A MODEL1 CONNECTED TO A VIEW1 AND A SEARCHER
            // GIVEN A MODEL2 CONNECTED TO A VIEW2 AND A SEARCHER
            model1.connectView(view1)
            model1.connectSearcherMultiQuery(searcher, attribute1)
            model2.connectView(view2)
            model2.connectSearcherMultiQuery(searcher, attribute2)

            // WHEN THE SEARCHER SEARCHES
            searcher.search()
            searcher.completed?.await()

            // EXPECT THE MODEL1 TO HAVE 2 REFINEMENTS
            // EXPECT THE MODEL2 TO HAVE 2 REFINEMENTS
            // EXPECT THE QUERY TO HAVE NO FILTERS
            model1.refinements.size shouldEqual 2
            model2.refinements.size shouldEqual 2
            query.filterBuilder.get().shouldBeEmpty()

            // WHEN THE VIEW1 CLICKED (Selecting refinement)
            view1.click(model1.refinements.first())
            searcher.completed?.await()

            // EXPECT THE QUERY TO HAVE FILTERS
            // EXPECT THE MODEL1 TO HAVE 1 REFINEMENT
            // EXPECT THE MODEL2 TO HAVE 2 REFINEMENT
            query.filterBuilder.get().shouldNotBeEmpty()
            model1.refinements.size shouldEqual 1
            model2.refinements.size shouldEqual 2

            // WHEN THE VIEW1 IS CLICKED (Deselecting its refinement)
            // THEN THE VIEW2 IS CLICKED (Selecting its refinement)
            view1.click(model1.refinements.first())
            view2.click(model2.refinements.first())
            searcher.completed?.await()

            // EXPECT THE QUERY TO HAVE FILTERS
            // EXPECT THE MODEL1 TO HAVE 2 REFINEMENT
            // EXPECT THE MODEL2 TO HAVE 1 REFINEMENT
            query.filterBuilder.get().shouldNotBeEmpty()
            model1.refinements.size shouldEqual 2
            model2.refinements.size shouldEqual 1

            // WHEN THE VIEW2 IS CLICKED (Deselecting its refinement)
            view2.click(model2.refinements.first())
            searcher.completed?.await()

            // EXPECT THE QUERY TO HAVE NO FILTERS
            // EXPECT THE MODEL1 TO HAVE 2 REFINEMENT
            // EXPECT THE MODEL2 TO HAVE 1 REFINEMENT
            query.filterBuilder.get().shouldBeEmpty()
            model1.refinements.size shouldEqual 2
            model2.refinements.size shouldEqual 2
        }
    }

    @Test
    fun searcherMultiQuery() {
        blocking {
            // Query1-> index1, Query2 -> index2
            // -> test widget1 displays refinements for 1, widget2 displays refinements for 2

            // GIVEN QUERIES MATCHING TWO RECORDS
            val attribute1 = Attribute("brand")
            val attribute2 = Attribute("color")
            val query1 = Query().apply { setFacets(attribute1) }
            val query2 = Query().apply { setFacets(attribute2) }
            val indexQuery1 = IndexQuery(index.indexName, query1)
            val indexQuery2 = IndexQuery(index2.indexName, query2)
            val searcher = SearcherMultiQuery(algolia, listOf(indexQuery1, indexQuery2))
            val model1 = RefinementModel<Facet>()
            val model2 = RefinementModel<Facet>()
            val view1 = MockView()
            val view2 = MockView()

            // GIVEN A MODEL1 CONNECTED TO A VIEW1 AND A SEARCHER
            // GIVEN A MODEL2 CONNECTED TO A VIEW2 AND A SEARCHER
            model1.connectView(view1)
            model1.connectSearcherMultiQuery(searcher, attribute1)
            model2.connectView(view2)
            model2.connectSearcherMultiQuery(searcher, attribute2)

            // WHEN THE SEARCHER SEARCHES
            searcher.search()
            searcher.completed?.await()

            // EXPECT THE MODEL1 TO HAVE 2 REFINEMENTS
            // EXPECT THE MODEL2 TO HAVE 2 REFINEMENTS
            // EXPECT THE QUERY TO HAVE NO FILTERS
//            model1.refinements.size shouldEqual 2
//            model2.refinements.size shouldEqual 2
//            query1.filterBuilder.get().shouldBeEmpty()
//            query2.filterBuilder.get().shouldBeEmpty()

            // WHEN THE VIEW1 CLICKED (Selecting refinement)
            view1.click(model1.refinements.first())
            searcher.completed?.await()

            // EXPECT THE QUERY TO HAVE FILTERS
            // EXPECT THE MODEL1 TO HAVE 1 REFINEMENT
            // EXPECT THE MODEL2 TO HAVE 2 REFINEMENT
//            query1.filterBuilder.get().shouldNotBeEmpty()
//            query2.filterBuilder.get().shouldBeEmpty()
//            model1.refinements.size shouldEqual 1
//            model2.refinements.size shouldEqual 1

            // WHEN THE VIEW1 IS CLICKED (Deselecting its refinement)
            // THEN THE VIEW2 IS CLICKED (Selecting its refinement)
            view1.click(model1.refinements.first())
            view2.click(model2.refinements.first())
            searcher.completed?.await()

            // EXPECT THE QUERY TO HAVE FILTERS
            // EXPECT THE MODEL1 TO HAVE 2 REFINEMENT
            // EXPECT THE MODEL2 TO HAVE 1 REFINEMENT
//            query1.filterBuilder.get().shouldBeEmpty()
//            query2.filterBuilder.get().shouldNotBeEmpty()
//            model1.refinements.size shouldEqual 2
//            model2.refinements.size shouldEqual 1

            // WHEN THE VIEW2 IS CLICKED (Deselecting its refinement)
            view2.click(model2.refinements.first())
            searcher.completed?.await()

            // EXPECT THE QUERY TO HAVE NO FILTERS
            // EXPECT THE MODEL1 TO HAVE 2 REFINEMENT
            // EXPECT THE MODEL2 TO HAVE 1 REFINEMENT
//            query1.filterBuilder.get().shouldBeEmpty()
//            query2.filterBuilder.get().shouldBeEmpty()
//            model1.refinements.size shouldEqual 2
//            model2.refinements.size shouldEqual 2


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